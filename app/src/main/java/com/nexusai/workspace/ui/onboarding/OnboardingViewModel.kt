package com.nexusai.workspace.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexusai.workspace.data.repository.UserPreferencesRepository
import com.nexusai.workspace.data.repository.WorkspaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val prefsRepository: UserPreferencesRepository,
    private val workspaceRepository: WorkspaceRepository
) : ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _selectedRole = MutableStateFlow("University Student")
    val selectedRole: StateFlow<String> = _selectedRole.asStateFlow()

    private val _selectedGoal = MutableStateFlow("Understand lecture notes & organize study plans")
    val selectedGoal: StateFlow<String> = _selectedGoal.asStateFlow()

    val availableRoles = listOf(
        "University Student",
        "Freelancer",
        "Early-Career Professional",
        "Solo Founder",
        "Lifelong Learner"
    )

    val goalsForRole = mapOf(
        "University Student" to listOf(
            "Understand lecture notes & organize study plans",
            "Prepare assignment checklists & exam revision",
            "Draft scholarship and internship emails"
        ),
        "Freelancer" to listOf(
            "Convert client briefs to milestone deliverables",
            "Draft project proposals and scope outlines",
            "Track weekly client tasks and invoice reminders"
        ),
        "Early-Career Professional" to listOf(
            "Meeting note summaries and action items",
            "Prepare career CV and application documents",
            "Organize daily priorities and work deadlines"
        ),
        "Solo Founder" to listOf(
            "Idea validation and SOP drafting",
            "Break down launch goals into actionable tasks",
            "Manage recurring operational checklists"
        ),
        "Lifelong Learner" to listOf(
            "Document Q&A and concept explainers",
            "Personal knowledge organization",
            "Structured learning schedules"
        )
    )

    fun onPageChanged(page: Int) {
        _currentPage.value = page
    }

    fun onNameChanged(name: String) {
        _userName.value = name
    }

    fun onRoleSelected(role: String) {
        _selectedRole.value = role
        val defaultGoals = goalsForRole[role] ?: emptyList()
        if (defaultGoals.isNotEmpty()) {
            _selectedGoal.value = defaultGoals.first()
        }
    }

    fun onGoalSelected(goal: String) {
        _selectedGoal.value = goal
    }

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            prefsRepository.saveOnboardingCompleted(
                userName = _userName.value.ifBlank { "Alex" },
                userRole = _selectedRole.value,
                primaryGoal = _selectedGoal.value
            )
            onSuccess()
        }
    }

    fun exploreAsGuest(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Seed initial sample demo action card so user can immediately see Workspace feature
            workspaceRepository.createActionCard(
                title = "Welcome to NexusAI Workspace",
                description = "This is an Action Card created from an AI suggestion. Tap to complete items or edit.",
                cardType = "TASK",
                items = listOf("Explore AI Chat", "Upload a PDF document", "Create your first Action Card"),
                dueDate = System.currentTimeMillis() + (86400000 * 2)
            )

            prefsRepository.saveOnboardingCompleted(
                userName = "Guest User",
                userRole = "University Student",
                primaryGoal = "Understand lecture notes & organize study plans"
            )
            onSuccess()
        }
    }
}
