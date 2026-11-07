package com.nexusai.workspace.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexusai.workspace.data.repository.UserPreferencesRepository
import com.nexusai.workspace.data.repository.WorkspaceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val prefsRepository: UserPreferencesRepository,
    private val workspaceRepository: WorkspaceRepository
) : ViewModel() {

    val userPreferences = prefsRepository.userPreferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun updateProfile(userName: String, userRole: String, primaryGoal: String) {
        viewModelScope.launch {
            prefsRepository.updateProfile(userName, userRole, primaryGoal)
        }
    }

    fun saveGeminiApiKey(apiKey: String) {
        viewModelScope.launch {
            prefsRepository.updateApiKey(apiKey)
        }
    }

    fun resetAllWorkspaceData(onResetDone: () -> Unit) {
        viewModelScope.launch {
            workspaceRepository.deleteAllCards()
            prefsRepository.saveOnboardingCompleted(
                userName = "User",
                userRole = "University Student",
                primaryGoal = "Understand lecture notes & organize study plans"
            )
            onResetDone()
        }
    }
}
