package com.nexusai.workspace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val fileUri: String,
    val pageCount: Int = 0,
    val fileSizeFormatted: String = "0 KB",
    val extractedContent: String = "", // Full text extracted from PDF
    val addedTimestamp: Long = System.currentTimeMillis()
)
