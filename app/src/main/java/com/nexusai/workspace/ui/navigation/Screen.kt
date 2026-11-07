package com.nexusai.workspace.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Onboarding : Screen("onboarding", "Onboarding")
    object Chat : Screen("chat", "AI Chat", Icons.Default.ChatBubbleOutline)
    object Documents : Screen("documents", "Documents", Icons.Default.Description)
    object Workspace : Screen("workspace", "Workspace", Icons.Default.FolderSpecial)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val BottomNavItems = listOf(
    Screen.Chat,
    Screen.Documents,
    Screen.Workspace,
    Screen.Settings
)
