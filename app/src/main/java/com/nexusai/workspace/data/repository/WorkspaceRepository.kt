package com.nexusai.workspace.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nexusai.workspace.data.local.dao.ActionCardDao
import com.nexusai.workspace.data.local.entity.ActionCardEntity
import kotlinx.coroutines.flow.Flow

class WorkspaceRepository(private val actionCardDao: ActionCardDao) {
    private val gson = Gson()

    val allCardsFlow: Flow<List<ActionCardEntity>> = actionCardDao.getAllCardsFlow()

    fun getCardsByTypeFlow(type: String): Flow<List<ActionCardEntity>> {
        return actionCardDao.getCardsByTypeFlow(type)
    }

    suspend fun createActionCard(
        title: String,
        description: String,
        cardType: String,
        items: List<String>,
        dueDate: Long?,
        sourceMessageId: Long? = null
    ): Long {
        val completedList = List(items.size) { false }
        val card = ActionCardEntity(
            title = title,
            description = description,
            cardType = cardType.uppercase(),
            status = "PENDING",
            dueDate = dueDate,
            checklistItemsJson = gson.toJson(items),
            checklistCompletedJson = gson.toJson(completedList),
            sourceMessageId = sourceMessageId
        )
        return actionCardDao.insertCard(card)
    }

    suspend fun toggleCardCompleted(cardId: Long) {
        val card = actionCardDao.getCardById(cardId) ?: return
        val newStatus = if (card.status == "COMPLETED") "PENDING" else "COMPLETED"
        actionCardDao.updateCard(card.copy(status = newStatus))
    }

    suspend fun toggleChecklistItem(cardId: Long, itemIndex: Int) {
        val card = actionCardDao.getCardById(cardId) ?: return
        val boolType = object : TypeToken<List<Boolean>>() {}.type
        val currentCompleted: MutableList<Boolean> = try {
            gson.fromJson<List<Boolean>>(card.checklistCompletedJson, boolType)?.toMutableList() ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }

        if (itemIndex in currentCompleted.indices) {
            currentCompleted[itemIndex] = !currentCompleted[itemIndex]
            val allDone = currentCompleted.all { it }
            val newStatus = if (allDone && currentCompleted.isNotEmpty()) "COMPLETED" else "IN_PROGRESS"
            actionCardDao.updateCard(
                card.copy(
                    checklistCompletedJson = gson.toJson(currentCompleted),
                    status = newStatus
                )
            )
        }
    }

    suspend fun updateCard(card: ActionCardEntity) {
        actionCardDao.updateCard(card)
    }

    suspend fun deleteCard(cardId: Long) {
        actionCardDao.deleteCard(cardId)
    }

    suspend fun deleteAllCards() {
        actionCardDao.deleteAllCards()
    }
}
