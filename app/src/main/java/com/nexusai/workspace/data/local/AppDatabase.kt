package com.nexusai.workspace.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nexusai.workspace.data.local.dao.ActionCardDao
import com.nexusai.workspace.data.local.dao.ChatDao
import com.nexusai.workspace.data.local.dao.DocumentDao
import com.nexusai.workspace.data.local.dao.UserPreferencesDao
import com.nexusai.workspace.data.local.entity.*

@Database(
    entities = [
        UserPreferenceEntity::class,
        ChatThreadEntity::class,
        ChatMessageEntity::class,
        DocumentEntity::class,
        ActionCardEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userPreferencesDao(): UserPreferencesDao
    abstract fun chatDao(): ChatDao
    abstract fun documentDao(): DocumentDao
    abstract fun actionCardDao(): ActionCardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nexus_ai_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
