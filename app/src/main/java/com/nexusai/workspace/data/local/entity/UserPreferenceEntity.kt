package com.nexusai.workspace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferenceEntity(
    @PrimaryKey
    val id: Int = 1,
    val hasCompletedOnboarding: Boolean = false,
    val userName: String = "User",
    val userRole: String = "University Student", // Student, Freelancer, Professional, Founder, Learner
    val primaryGoal: String = "Understand lecture notes & organize tasks",
    val geminiApiKey: String = "",
    val dailyPromptCount: Int = 0,
    val maxDailyPrompts: Int = 20,
    val lastPromptResetDate: String = ""
)
