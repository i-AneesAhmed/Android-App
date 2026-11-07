package com.nexusai.workspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nexusai.workspace.data.repository.UserPreferencesRepository
import com.nexusai.workspace.ui.navigation.NexusNavHost
import com.nexusai.workspace.ui.theme.IndigoPrimary
import com.nexusai.workspace.ui.theme.NexusAITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = (application as NexusAIApp).database
        val userPrefsRepo = UserPreferencesRepository(db.userPreferencesDao())

        setContent {
            NexusAITheme {
                var onboardingCompleted by remember { mutableStateOf<Boolean?>(null) }

                LaunchedEffect(Unit) {
                    val prefs = userPrefsRepo.getUserPreferences()
                    onboardingCompleted = prefs.hasCompletedOnboarding
                }

                if (onboardingCompleted == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = IndigoPrimary)
                    }
                } else {
                    NexusNavHost(hasCompletedOnboarding = onboardingCompleted == true)
                }
            }
        }
    }
}
