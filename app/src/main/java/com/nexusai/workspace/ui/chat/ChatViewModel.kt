package com.nexusai.workspace.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nexusai.workspace.data.ai.SuggestedAction
import com.nexusai.workspace.data.local.entity.ChatMessageEntity
import com.nexusai.workspace.data.local.entity.ChatThreadEntity
import com.nexusai.workspace.data.repository.ChatRepository
import com.nexusai.workspace.data.repository.UserPreferencesRepository
import com.nexusai.workspace.data.repository.WorkspaceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val prefsRepository: UserPreferencesRepository,
    private val workspaceRepository: WorkspaceRepository
) : ViewModel() {

    private val gson = Gson()

    val userPreferences = prefsRepository.userPreferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val allThreads = chatRepository.allThreadsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _activeThreadId = MutableStateFlow<Long?>(null)
    val activeThreadId: StateFlow<Long?> = _activeThreadId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeMessages: StateFlow<List<ChatMessageEntity>> = _activeThreadId.flatMapLatest { id ->
        if (id != null) chatRepository.getMessagesForThreadFlow(id) else flowOf(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _quotaExceededError = MutableStateFlow(false)
    val quotaExceededError: StateFlow<Boolean> = _quotaExceededError.asStateFlow()

    // Action Card Creation Modal State
    private val _pendingActionCard = MutableStateFlow<SuggestedAction?>(null)
    val pendingActionCard: StateFlow<SuggestedAction?> = _pendingActionCard.asStateFlow()

    init {
        // Auto-select or create first thread
        viewModelScope.launch {
            allThreads.collect { threads ->
                if (_activeThreadId.value == null && threads.isNotEmpty()) {
                    _activeThreadId.value = threads.first().id
                } else if (threads.isEmpty()) {
                    createNewThread("General Workspace AI")
                }
            }
        }
    }

    fun createNewThread(title: String = "New Conversation", boundDocId: Long? = null) {
        viewModelScope.launch {
            val newId = chatRepository.createThread(title, boundDocId)
            _activeThreadId.value = newId
        }
    }

    fun selectThread(threadId: Long) {
        _activeThreadId.value = threadId
    }

    fun deleteThread(threadId: Long) {
        viewModelScope.launch {
            chatRepository.deleteThread(threadId)
            if (_activeThreadId.value == threadId) {
                val remaining = allThreads.value.filter { it.id != threadId }
                _activeThreadId.value = remaining.firstOrNull()?.id
            }
        }
    }

    fun sendMessage(promptText: String, docContext: String? = null) {
        val currentPrompt = promptText.trim()
        if (currentPrompt.isBlank()) return

        viewModelScope.launch {
            val canSend = prefsRepository.canSendPrompt()
            if (!canSend) {
                _quotaExceededError.value = true
                return@launch
            }
            _quotaExceededError.value = false

            val threadId = _activeThreadId.value ?: chatRepository.createThread("AI Chat Thread").also {
                _activeThreadId.value = it
            }

            _isGenerating.value = true
            prefsRepository.incrementDailyPromptCount()

            val prefs = prefsRepository.getUserPreferences()
            try {
                chatRepository.sendMessage(
                    threadId = threadId,
                    userPrompt = currentPrompt,
                    userRole = prefs.userRole,
                    primaryGoal = prefs.primaryGoal,
                    geminiApiKey = prefs.geminiApiKey,
                    documentContext = docContext
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun dismissQuotaError() {
        _quotaExceededError.value = false
    }

    fun openActionCardPreview(suggestedActionJson: String) {
        try {
            val action = gson.fromJson(suggestedActionJson, SuggestedAction::class.java)
            _pendingActionCard.value = action
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissActionCardPreview() {
        _pendingActionCard.value = null
    }

    fun confirmSaveActionCard(
        title: String,
        description: String,
        cardType: String,
        items: List<String>,
        dueDaysFromNow: Int
    ) {
        viewModelScope.launch {
            val dueDateMillis = System.currentTimeMillis() + (dueDaysFromNow * 86400000L)
            workspaceRepository.createActionCard(
                title = title,
                description = description,
                cardType = cardType,
                items = items,
                dueDate = dueDateMillis
            )
            _pendingActionCard.value = null
        }
    }
}
