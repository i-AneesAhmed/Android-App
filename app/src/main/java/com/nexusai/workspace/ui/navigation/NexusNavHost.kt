package com.nexusai.workspace.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nexusai.workspace.NexusAIApp
import com.nexusai.workspace.ui.ViewModelFactory
import com.nexusai.workspace.ui.chat.ChatScreen
import com.nexusai.workspace.ui.chat.ChatViewModel
import com.nexusai.workspace.ui.documents.DocumentDetailScreen
import com.nexusai.workspace.ui.documents.DocumentListScreen
import com.nexusai.workspace.ui.documents.DocumentViewModel
import com.nexusai.workspace.ui.onboarding.OnboardingScreen
import com.nexusai.workspace.ui.onboarding.OnboardingViewModel
import com.nexusai.workspace.ui.settings.SettingsScreen
import com.nexusai.workspace.ui.settings.SettingsViewModel
import com.nexusai.workspace.ui.workspace.WorkspaceScreen
import com.nexusai.workspace.ui.workspace.WorkspaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexusNavHost(
    hasCompletedOnboarding: Boolean,
    factory: ViewModelFactory = ViewModelFactory(NexusAIApp.instance)
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val onboardingViewModel: OnboardingViewModel = viewModel(factory = factory)
    val chatViewModel: ChatViewModel = viewModel(factory = factory)
    val documentViewModel: DocumentViewModel = viewModel(factory = factory)
    val workspaceViewModel: WorkspaceViewModel = viewModel(factory = factory)
    val settingsViewModel: SettingsViewModel = viewModel(factory = factory)

    val startDestination = if (hasCompletedOnboarding) Screen.Chat.route else Screen.Onboarding.route

    val showBottomBar = currentRoute in listOf(
        Screen.Chat.route,
        Screen.Documents.route,
        Screen.Workspace.route,
        Screen.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    BottomNavItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                screen.icon?.let { icon ->
                                    Icon(imageVector = icon, contentDescription = screen.title)
                                }
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    viewModel = onboardingViewModel,
                    onOnboardingCompleted = {
                        navController.navigate(Screen.Chat.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Chat.route) {
                ChatScreen(viewModel = chatViewModel)
            }

            composable(Screen.Documents.route) {
                DocumentListScreen(
                    viewModel = documentViewModel,
                    onDocumentClick = {
                        navController.navigate("document_detail")
                    },
                    onStartDocChat = { threadId ->
                        chatViewModel.selectThread(threadId)
                        navController.navigate(Screen.Chat.route)
                    }
                )
            }

            composable("document_detail") {
                DocumentDetailScreen(
                    viewModel = documentViewModel,
                    onBackClick = { navController.popBackStack() },
                    onStartDocChat = { threadId ->
                        chatViewModel.selectThread(threadId)
                        navController.navigate(Screen.Chat.route)
                    }
                )
            }

            composable(Screen.Workspace.route) {
                WorkspaceScreen(viewModel = workspaceViewModel)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }
}
