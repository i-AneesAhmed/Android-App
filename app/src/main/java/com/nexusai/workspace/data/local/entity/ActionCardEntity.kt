package com.nexusai.workspace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "action_cards")
data class ActionCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val cardType: String, // "TASK", "CHECKLIST", "STUDY_PLAN", "REMINDER"
    val status: String = "PENDING", // "PENDING", "IN_PROGRESS", "COMPLETED"
    val dueDate: Long? = null,
    val checklistItemsJson: String = "[]", // JSON array of items e.g. ["Item 1", "Item 2"]
    val checklistCompletedJson: String = "[]", // JSON array of booleans matching items
    val createdTimestamp: Long = System.currentTimeMillis(),
    val sourceMessageId: Long? = null
)
