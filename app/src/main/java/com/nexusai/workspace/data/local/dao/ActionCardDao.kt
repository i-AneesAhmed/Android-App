package com.nexusai.workspace.data.local.dao

import androidx.room.*
import com.nexusai.workspace.data.local.entity.ActionCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionCardDao {
    @Query("SELECT * FROM action_cards ORDER BY createdTimestamp DESC")
    fun getAllCardsFlow(): Flow<List<ActionCardEntity>>

    @Query("SELECT * FROM action_cards WHERE cardType = :type ORDER BY createdTimestamp DESC")
    fun getCardsByTypeFlow(type: String): Flow<List<ActionCardEntity>>

    @Query("SELECT * FROM action_cards WHERE id = :cardId LIMIT 1")
    suspend fun getCardById(cardId: Long): ActionCardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: ActionCardEntity): Long

    @Update
    suspend fun updateCard(card: ActionCardEntity)

    @Query("DELETE FROM action_cards WHERE id = :cardId")
    suspend fun deleteCard(cardId: Long)

    @Query("DELETE FROM action_cards")
    suspend fun deleteAllCards()
}
