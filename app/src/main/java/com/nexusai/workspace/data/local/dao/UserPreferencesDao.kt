package com.nexusai.workspace.data.local.dao

import androidx.room.*
import com.nexusai.workspace.data.local.entity.UserPreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    fun getUserPreferencesFlow(): Flow<UserPreferenceEntity?>

    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    suspend fun getUserPreferences(): UserPreferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(preferences: UserPreferenceEntity)

    @Query("UPDATE user_preferences SET geminiApiKey = :apiKey WHERE id = 1")
    suspend fun updateApiKey(apiKey: String)

    @Query("UPDATE user_preferences SET dailyPromptCount = :count, lastPromptResetDate = :resetDate WHERE id = 1")
    suspend fun updatePromptQuota(count: Int, resetDate: String)
}
