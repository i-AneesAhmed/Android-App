package com.nexusai.workspace.data.local.dao

import androidx.room.*
import com.nexusai.workspace.data.local.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY addedTimestamp DESC")
    fun getAllDocumentsFlow(): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE id = :docId LIMIT 1")
    suspend fun getDocumentById(docId: Long): DocumentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentEntity): Long

    @Query("DELETE FROM documents WHERE id = :docId")
    suspend fun deleteDocument(docId: Long)
}
