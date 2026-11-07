package com.nexusai.workspace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nexusai.workspace.NexusAIApp
import com.nexusai.workspace.data.repository.ChatRepository
import com.nexusai.workspace.data.repository.DocumentRepository
import com.nexusai.workspace.data.repository.UserPreferencesRepository
import com.nexusai.workspace.data.repository.WorkspaceRepository
import com.nexusai.workspace.ui.chat.ChatViewModel
import com.nexusai.workspace.ui.documents.DocumentViewModel
import com.nexusai.workspace.ui.onboarding.OnboardingViewModel
import com.nexusai.workspace.ui.settings.SettingsViewModel
import com.nexusai.workspace.ui.workspace.WorkspaceViewModel

class ViewModelFactory(private val app: NexusAIApp) : ViewModelProvider.Factory {

    private val db = app.database

    private val userPrefsRepo by lazy { UserPreferencesRepository(db.userPreferencesDao()) }
    private val chatRepo by lazy { ChatRepository(db.chatDao()) }
    private val docRepo by lazy { DocumentRepository(db.documentDao()) }
    private val workspaceRepo by lazy { WorkspaceRepository(db.actionCardDao()) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) -> {
                OnboardingViewModel(userPrefsRepo, workspaceRepo) as T
            }
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(chatRepo, userPrefsRepo, workspaceRepo) as T
            }
            modelClass.isAssignableFrom(DocumentViewModel::class.java) -> {
                DocumentViewModel(docRepo, chatRepo) as T
            }
            modelClass.isAssignableFrom(WorkspaceViewModel::class.java) -> {
                WorkspaceViewModel(workspaceRepo) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(userPrefsRepo, workspaceRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
