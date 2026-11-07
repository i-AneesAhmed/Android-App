package com.nexusai.workspace

import android.app.Application
import com.nexusai.workspace.data.local.AppDatabase

class NexusAIApp : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: NexusAIApp
            private set
    }
}
