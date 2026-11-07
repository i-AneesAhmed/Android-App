package com.nexusai.workspace.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexusai.workspace.ui.chat.components.ActionCardPreviewDialog
import com.nexusai.workspace.ui.chat.components.ChatMessageBubble
import com.nexusai.workspace.ui.chat.components.QuotaIndicatorBar
import com.nexusai.workspace.ui.theme.IndigoPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel
) {
    val userPrefs by viewModel.userPreferences.collectAsState()
    val threads by viewModel.allThreads.collectAsState()
    val activeThreadId by viewModel.activeThreadId.collectAsState()
    val messages by viewModel.activeMessages.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val quotaExceeded by viewModel.quotaExceededError.collectAsState()
    val pendingAction by viewModel.pendingActionCard.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var showThreadDrawer by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val promptSuggestions = listOf(
        "📚 Create a 7-day study plan",
        "💼 Draft a client project brief",
        "📄 Summarize document key takeaways",
        "✅ Create assignment checklist",
        "🌐 Translate to Urdu / Roman Urdu"
    )

    // Scroll to bottom when messages update
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = threads.find { it.id == activeThreadId }?.title ?: "AI Chat Workspace",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${userPrefs?.userRole ?: "Workspace"} • ${userPrefs?.primaryGoal?.take(25) ?: ""}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showThreadDrawer = true }) {
                        Icon(Icons.Default.Forum, contentDescription = "History Threads")
                    }
                    IconButton(onClick = { viewModel.createNewThread() }) {
                        Icon(Icons.Default.Add, contentDescription = "New Chat")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Quota bar
                QuotaIndicatorBar(
                    usedCount = userPrefs?.dailyPromptCount ?: 0,
                    maxCount = userPrefs?.maxDailyPrompts ?: 20
                )

                // Message List or Empty Banner
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (messages.isEmpty() && !isGenerating) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = IndigoPrimary,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "What would you like to plan today?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ask NexusAI to generate study plans, draft client briefs, summarize notes, or manage your personal workspace.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(messages, key = { it.id }) { msg ->
                                ChatMessageBubble(
                                    message = msg,
                                    onConvertToCardClick = { json -> viewModel.openActionCardPreview(json) }
                                )
                            }

                            if (isGenerating) {
                                item {
                                    Row(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            strokeWidth = 2.dp,
                                            color = IndigoPrimary
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "NexusAI is thinking & organizing...",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Quick Suggestions Chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(promptSuggestions) { prompt ->
                        SuggestionChip(
                            onClick = {
                                inputText = prompt.substringAfter(" ")
                            },
                            label = { Text(prompt, fontSize = 12.sp) }
                        )
                    }
                }

                // Text Input Footer Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Ask NexusAI anything...") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            shape = RoundedCornerShape(24.dp),
                            maxLines = 4
                        )

                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    val textToSend = inputText
                                    inputText = ""
                                    viewModel.sendMessage(textToSend)
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(IndigoPrimary),
                            enabled = inputText.isNotBlank() && !isGenerating
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // Quota Exceeded Snackbar / Dialog
            if (quotaExceeded) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissQuotaError() },
                    title = { Text("Daily Quota Reached") },
                    text = { Text("You've used all 20 free daily prompts. Your quota will reset automatically tomorrow, or you can add your custom Gemini API key in Settings for unlimited queries!") },
                    confirmButton = {
                        Button(onClick = { viewModel.dismissQuotaError() }) {
                            Text("Got it")
                        }
                    }
                )
            }

            // Action Card Creation Confirmation Dialog
            pendingAction?.let { action ->
                ActionCardPreviewDialog(
                    action = action,
                    onDismiss = { viewModel.dismissActionCardPreview() },
                    onConfirmSave = { title, desc, type, items, dueDays ->
                        viewModel.confirmSaveActionCard(title, desc, type, items, dueDays)
                    }
                )
            }

            // Thread Switcher Bottom Sheet
            if (showThreadDrawer) {
                ModalBottomSheet(onDismissRequest = { showThreadDrawer = false }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Conversation Threads",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Button(
                                onClick = {
                                    showThreadDrawer = false
                                    viewModel.createNewThread()
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Chat")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                            items(threads) { thread ->
                                val isSelected = thread.id == activeThreadId
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            viewModel.selectThread(thread.id)
                                            showThreadDrawer = false
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = thread.title,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.weight(1f)
                                        )

                                        IconButton(onClick = { viewModel.deleteThread(thread.id) }) {
                                            Icon(
                                                imageVector = Icons.Default.DeleteOutline,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
