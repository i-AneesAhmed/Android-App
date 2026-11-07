package com.nexusai.workspace.ui.workspace

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nexusai.workspace.data.local.entity.ActionCardEntity
import com.nexusai.workspace.data.repository.WorkspaceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkspaceViewModel(
    private val workspaceRepository: WorkspaceRepository
) : ViewModel() {

    private val gson = Gson()

    val allCards = workspaceRepository.allCardsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedTypeFilter = MutableStateFlow("ALL")
    val selectedTypeFilter: StateFlow<String> = _selectedTypeFilter.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow("ALL") // "ALL", "PENDING", "COMPLETED"
    val selectedStatusFilter: StateFlow<String> = _selectedStatusFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _cardToEdit = MutableStateFlow<ActionCardEntity?>(null)
    val cardToEdit: StateFlow<ActionCardEntity?> = _cardToEdit.asStateFlow()

    val filteredCards = combine(allCards, _selectedTypeFilter, _selectedStatusFilter, _searchQuery) { cards, type, status, query ->
        cards.filter { card ->
            val matchesType = if (type == "ALL") true else card.cardType == type
            val matchesStatus = if (status == "ALL") true else card.status == status
            val matchesSearch = if (query.isBlank()) true else card.title.contains(query, ignoreCase = true) || card.description.contains(query, ignoreCase = true)
            matchesType && matchesStatus && matchesSearch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setTypeFilter(type: String) {
        _selectedTypeFilter.value = type
    }

    fun setStatusFilter(status: String) {
        _selectedStatusFilter.value = status
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleCardCompleted(cardId: Long) {
        viewModelScope.launch {
            workspaceRepository.toggleCardCompleted(cardId)
        }
    }

    fun toggleChecklistItem(cardId: Long, itemIndex: Int) {
        viewModelScope.launch {
            workspaceRepository.toggleChecklistItem(cardId, itemIndex)
        }
    }

    fun openEditCard(card: ActionCardEntity) {
        _cardToEdit.value = card
    }

    fun dismissEditCard() {
        _cardToEdit.value = null
    }

    fun saveEditedCard(card: ActionCardEntity) {
        viewModelScope.launch {
            workspaceRepository.updateCard(card)
            _cardToEdit.value = null
        }
    }

    fun deleteCard(cardId: Long) {
        viewModelScope.launch {
            workspaceRepository.deleteCard(cardId)
        }
    }

    fun exportCardAsText(context: Context, card: ActionCardEntity) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("NexusAI Action Card: ${card.title}\n")
        stringBuilder.append("Type: ${card.cardType}\n")
        if (card.description.isNotBlank()) stringBuilder.append("Description: ${card.description}\n")

        try {
            val listType = object : TypeToken<List<String>>() {}.type
            val items: List<String> = gson.fromJson(card.checklistItemsJson, listType) ?: emptyList()
            val boolType = object : TypeToken<List<Boolean>>() {}.type
            val completed: List<Boolean> = gson.fromJson(card.checklistCompletedJson, boolType) ?: emptyList()

            if (items.isNotEmpty()) {
                stringBuilder.append("\nChecklist Items:\n")
                items.forEachIndexed { idx, item ->
                    val isDone = if (idx < completed.size) completed[idx] else false
                    val mark = if (isDone) "[x]" else "[ ]"
                    stringBuilder.append("$mark $item\n")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Workspace Item")
        context.startActivity(shareIntent)
    }

    fun createManualActionCard(
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
        }
    }
}
