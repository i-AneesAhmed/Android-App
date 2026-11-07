package com.nexusai.workspace.ui.workspace.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nexusai.workspace.data.local.entity.ActionCardEntity
import com.nexusai.workspace.ui.theme.IndigoPrimary
import com.nexusai.workspace.ui.theme.PurpleAccent
import com.nexusai.workspace.ui.theme.TealAccent
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ActionCardItem(
    card: ActionCardEntity,
    onToggleCompleted: () -> Unit,
    onToggleChecklistItem: (Int) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onExportClick: () -> Unit
) {
    val gson = remember { Gson() }
    val isCompleted = card.status == "COMPLETED"

    val items: List<String> = remember(card.checklistItemsJson) {
        try {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson(card.checklistItemsJson, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    val completedState: List<Boolean> = remember(card.checklistCompletedJson) {
        try {
            val listType = object : TypeToken<List<Boolean>>() {}.type
            gson.fromJson(card.checklistCompletedJson, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    val dateFormatted = remember(card.dueDate) {
        card.dueDate?.let {
            SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(it))
        }
    }

    val badgeColor = when (card.cardType) {
        "STUDY_PLAN" -> IndigoPrimary
        "CHECKLIST" -> TealAccent
        "REMINDER" -> PurpleAccent
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Badge & Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = badgeColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = card.cardType.replace("_", " "),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onExportClick) {
                        Icon(Icons.Default.Share, contentDescription = "Share/Export", modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Title + Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { onToggleCompleted() }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )

                    if (card.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = card.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    dateFormatted?.let { date ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Due: $date",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Interactive Checklist Items if available
            if (items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(8.dp))

                items.forEachIndexed { idx, itemText ->
                    val checked = if (idx < completedState.size) completedState[idx] else false
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleChecklistItem(idx) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (checked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                            contentDescription = null,
                            tint = if (checked) TealAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = itemText,
                            style = MaterialTheme.typography.bodyMedium,
                            textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                            color = if (checked) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
