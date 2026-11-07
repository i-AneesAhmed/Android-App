package com.nexusai.workspace.ui.workspace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexusai.workspace.ui.theme.IndigoPrimary
import com.nexusai.workspace.ui.theme.TealAccent
import com.nexusai.workspace.ui.workspace.components.ActionCardItem
import com.nexusai.workspace.ui.workspace.components.EditActionCardDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceScreen(
    viewModel: WorkspaceViewModel
) {
    val cards by viewModel.filteredCards.collectAsState()
    val allCards by viewModel.allCards.collectAsState()
    val selectedType by viewModel.selectedTypeFilter.collectAsState()
    val selectedStatus by viewModel.selectedStatusFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val cardToEdit by viewModel.cardToEdit.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val types = listOf("ALL", "TASK", "CHECKLIST", "STUDY_PLAN", "REMINDER")
    val statuses = listOf("ALL", "PENDING", "COMPLETED")

    val totalCount = allCards.size
    val completedCount = allCards.count { it.status == "COMPLETED" }
    val pendingCount = totalCount - completedCount

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearching) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("Search workspace items...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Column {
                            Text("Personal Workspace", fontWeight = FontWeight.Bold)
                            Text(
                                "$completedCount completed • $pendingCount pending",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { isSearching = !isSearching }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = IndigoPrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Card")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Stats Overview Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoPrimary.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$totalCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = IndigoPrimary)
                    }
                    Divider(modifier = Modifier.height(30.dp).width(1.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Pending", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$pendingCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    Divider(modifier = Modifier.height(30.dp).width(1.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Done", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$completedCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TealAccent)
                    }
                }
            }

            // Type Filter Chips Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(types) { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { viewModel.setTypeFilter(type) },
                        label = { Text(if (type == "ALL") "All Types" else type.replace("_", " "), fontSize = 12.sp) }
                    )
                }
            }

            // Status Filter Chips Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(statuses) { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = { viewModel.setStatusFilter(status) },
                        label = { Text(if (status == "ALL") "All Status" else status, fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main List
            if (cards.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FolderSpecial,
                            contentDescription = null,
                            tint = IndigoPrimary,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No action cards found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Convert an AI chat answer or tap '+' to create a new card.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(cards, key = { it.id }) { card ->
                        ActionCardItem(
                            card = card,
                            onToggleCompleted = { viewModel.toggleCardCompleted(card.id) },
                            onToggleChecklistItem = { idx -> viewModel.toggleChecklistItem(card.id, idx) },
                            onEditClick = { viewModel.openEditCard(card) },
                            onDeleteClick = { viewModel.deleteCard(card.id) },
                            onExportClick = { viewModel.exportCardAsText(context, card) }
                        )
                    }
                }
            }
        }

        // Edit Card Dialog
        cardToEdit?.let { card ->
            EditActionCardDialog(
                card = card,
                onDismiss = { viewModel.dismissEditCard() },
                onSave = { updated -> viewModel.saveEditedCard(updated) }
            )
        }

        // Manual Create Dialog
        if (showCreateDialog) {
            var newTitle by remember { mutableStateOf("") }
            var newDesc by remember { mutableStateOf("") }
            var newType by remember { mutableStateOf("TASK") }
            var newItemsText by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = { Text("New Action Card") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newDesc,
                            onValueChange = { newDesc = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newItemsText,
                            onValueChange = { newItemsText = it },
                            label = { Text("Checklist Items (1 per line)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newTitle.isNotBlank()) {
                                val items = newItemsText.split("\n").map { it.trim() }.filter { it.isNotBlank() }
                                viewModel.createManualActionCard(
                                    title = newTitle,
                                    description = newDesc,
                                    cardType = newType,
                                    items = items,
                                    dueDaysFromNow = 2
                                )
                                showCreateDialog = false
                            }
                        }
                    ) {
                        Text("Create Card")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
