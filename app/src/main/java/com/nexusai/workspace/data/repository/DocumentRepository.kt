package com.nexusai.workspace.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.nexusai.workspace.data.ai.PdfTextExtractor
import com.nexusai.workspace.data.local.dao.DocumentDao
import com.nexusai.workspace.data.local.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow

class DocumentRepository(private val documentDao: DocumentDao) {

    val allDocumentsFlow: Flow<List<DocumentEntity>> = documentDao.getAllDocumentsFlow()

    suspend fun getDocumentById(id: Long): DocumentEntity? {
        return documentDao.getDocumentById(id)
    }

    suspend fun importDocument(context: Context, uri: Uri): Long {
        val title = getFileNameFromUri(context, uri) ?: "Document_${System.currentTimeMillis()}.pdf"
        val extracted = PdfTextExtractor.extractTextFromUri(context, uri)

        val entity = DocumentEntity(
            title = title,
            fileUri = uri.toString(),
            pageCount = extracted.pageCount,
            fileSizeFormatted = "${(extracted.fullText.length / 1024).coerceAtLeast(12)} KB",
            extractedContent = extracted.fullText
        )
        return documentDao.insertDocument(entity)
    }

    suspend fun deleteDocument(id: Long) {
        documentDao.deleteDocument(id)
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var name: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        name = cursor.getString(index)
                    }
                }
            }
        }
        if (name == null) {
            name = uri.path?.let { path ->
                val cut = path.lastIndexOf('/')
                if (cut != -1) path.substring(cut + 1) else path
            }
        }
        return name
    }
}
