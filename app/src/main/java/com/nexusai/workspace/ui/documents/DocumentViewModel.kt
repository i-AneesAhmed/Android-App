package com.nexusai.workspace.ui.documents

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexusai.workspace.data.local.entity.DocumentEntity
import com.nexusai.workspace.data.repository.ChatRepository
import com.nexusai.workspace.data.repository.DocumentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DocumentViewModel(
    private val documentRepository: DocumentRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    val documents = documentRepository.allDocumentsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedDocument = MutableStateFlow<DocumentEntity?>(null)
    val selectedDocument: StateFlow<DocumentEntity?> = _selectedDocument.asStateFlow()

    private val _isImporting = MutableStateFlow(false)
    val isImporting: StateFlow<Boolean> = _isImporting.asStateFlow()

    fun selectDocument(doc: DocumentEntity?) {
        _selectedDocument.value = doc
    }

    fun importPdfDocument(context: Context, uri: Uri, onImported: (Long) -> Unit) {
        viewModelScope.launch {
            _isImporting.value = true
            try {
                val docId = documentRepository.importDocument(context, uri)
                val doc = documentRepository.getDocumentById(docId)
                _selectedDocument.value = doc
                onImported(docId)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isImporting.value = false
            }
        }
    }

    fun deleteDocument(docId: Long) {
        viewModelScope.launch {
            documentRepository.deleteDocument(docId)
            if (_selectedDocument.value?.id == docId) {
                _selectedDocument.value = null
            }
        }
    }

    fun startDocumentChat(doc: DocumentEntity, onNavigateToChat: (Long) -> Unit) {
        viewModelScope.launch {
            val threadTitle = "Doc Q&A: ${doc.title}"
            val threadId = chatRepository.createThread(threadTitle, boundDocumentId = doc.id)
            onNavigateToChat(threadId)
        }
    }
}
