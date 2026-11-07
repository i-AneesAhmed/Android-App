package com.nexusai.workspace.data.repository

import com.google.gson.Gson
import com.nexusai.workspace.data.ai.AiEngine
import com.nexusai.workspace.data.local.dao.ChatDao
import com.nexusai.workspace.data.local.entity.ChatMessageEntity
import com.nexusai.workspace.data.local.entity.ChatThreadEntity
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val chatDao: ChatDao,
    private val aiEngine: AiEngine = AiEngine()
) {
    private val gson = Gson()

    val allThreadsFlow: Flow<List<ChatThreadEntity>> = chatDao.getAllThreadsFlow()

    fun getMessagesForThreadFlow(threadId: Long): Flow<List<ChatMessageEntity>> {
        return chatDao.getMessagesForThreadFlow(threadId)
    }

    suspend fun createThread(title: String, boundDocumentId: Long? = null): Long {
        val thread = ChatThreadEntity(
            title = title.ifBlank { "New Workspace Chat" },
            boundDocumentId = boundDocumentId
        )
        return chatDao.insertThread(thread)
    }

    suspend fun deleteThread(threadId: Long) {
        chatDao.deleteMessagesForThread(threadId)
        chatDao.deleteThread(threadId)
    }

    suspend fun sendMessage(
        threadId: Long,
        userPrompt: String,
        userRole: String,
        primaryGoal: String,
        geminiApiKey: String,
        documentContext: String? = null
    ): ChatMessageEntity {
        // 1. Save User Message
        val userMsg = ChatMessageEntity(
            threadId = threadId,
            sender = "USER",
            content = userPrompt
        )
        chatDao.insertMessage(userMsg)

        // 2. Query AI Engine
        val aiResponse = aiEngine.processUserPrompt(
            prompt = userPrompt,
            userRole = userRole,
            userGoal = primaryGoal,
            geminiApiKey = geminiApiKey,
            documentContext = documentContext
        )

        // 3. Serialize suggested Action Card if present
        val suggestedJson = aiResponse.suggestedAction?.let { gson.toJson(it) }

        // 4. Save AI Response
        val aiMsg = ChatMessageEntity(
            threadId = threadId,
            sender = "AI",
            content = aiResponse.replyText,
            suggestedActionJson = suggestedJson
        )
        val aiMsgId = chatDao.insertMessage(aiMsg)

        // Update thread updated timestamp
        val thread = chatDao.getThreadById(threadId)
        if (thread != null) {
            chatDao.updateThread(thread.copy(updatedTimestamp = System.currentTimeMillis()))
        }

        return aiMsg.copy(id = aiMsgId)
    }
}
