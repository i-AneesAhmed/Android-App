package com.nexusai.workspace.data.repository

import com.nexusai.workspace.data.local.dao.UserPreferencesDao
import com.nexusai.workspace.data.local.entity.UserPreferenceEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class UserPreferencesRepository(private val dao: UserPreferencesDao) {

    val userPreferencesFlow: Flow<UserPreferenceEntity?> = dao.getUserPreferencesFlow()

    suspend fun getUserPreferences(): UserPreferenceEntity {
        val prefs = dao.getUserPreferences()
        if (prefs == null) {
            val defaultPrefs = UserPreferenceEntity()
            dao.insertOrUpdate(defaultPrefs)
            return defaultPrefs
        }
        return prefs
    }

    suspend fun saveOnboardingCompleted(userName: String, userRole: String, primaryGoal: String) {
        val current = getUserPreferences()
        val updated = current.copy(
            hasCompletedOnboarding = true,
            userName = userName.ifBlank { "User" },
            userRole = userRole,
            primaryGoal = primaryGoal
        )
        dao.insertOrUpdate(updated)
    }

    suspend fun updateApiKey(apiKey: String) {
        dao.updateApiKey(apiKey.trim())
    }

    suspend fun canSendPrompt(): Boolean {
        val prefs = getUserPreferences()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (prefs.lastPromptResetDate != today) {
            // Reset daily quota for new day
            dao.updatePromptQuota(0, today)
            return true
        }
        return prefs.dailyPromptCount < prefs.maxDailyPrompts
    }

    suspend fun incrementDailyPromptCount() {
        val prefs = getUserPreferences()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val newCount = if (prefs.lastPromptResetDate == today) prefs.dailyPromptCount + 1 else 1
        dao.updatePromptQuota(newCount, today)
    }

    suspend fun updateProfile(userName: String, userRole: String, primaryGoal: String) {
        val current = getUserPreferences()
        val updated = current.copy(
            userName = userName,
            userRole = userRole,
            primaryGoal = primaryGoal
        )
        dao.insertOrUpdate(updated)
    }
}
