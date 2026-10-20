package com.example.tugasana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugasana.ui.TugasViewModel

data class TaskItem(
    val id: Int,
    val title: String,
    val category: String,
    val status: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: TugasViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var showAddDialog by remember { mutableStateOf(false) }

    val categories = listOf("Semua", "Tugas Utama", "Proyek", "Catatan")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header Welcome
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Selamat Datang, ${viewModel.userName}! 👋",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ada ${viewModel.tasks.count { it.status != "Selesai" }} tugas yang belum selesai hari ini.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari tugas atau catatan...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = category == selectedCategory,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Tugas",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${viewModel.tasks.size} item",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tasks List
            val filteredTasks = viewModel.tasks.filter { task ->
                (selectedCategory == "Semua" || task.category == selectedCategory) &&
                        (searchQuery.isEmpty() || task.title.contains(searchQuery, ignoreCase = true))
            }

            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada tugas ditemukan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = filteredTasks,
                        key = { it.id }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onToggleStatus = { viewModel.toggleTaskStatus(task.id) },
                            onDelete = { viewModel.deleteTask(task.id) }
                        )
                    }
                }
            }
        }

        // Floating Action Button to Add New Task
        ExtendedFloatingActionButton(
            onClick = { showAddDialog = true },
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("Tambah Tugas") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )

        // Dialog Input Tugas Baru
        if (showAddDialog) {
            AddTaskDialog(
                onDismiss = { showAddDialog = false },
                onTaskAdded = { title, category, status, date ->
                    viewModel.addTask(title, category, status, date)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun TaskCard(
    task: TaskItem,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleStatus() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (task.status == "Selesai")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    )
                    .clickable { onToggleStatus() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (task.status == "Selesai") Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.List,
                    contentDescription = null,
                    tint = if (task.status == "Selesai")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "• ${task.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AssistChip(
                onClick = onToggleStatus,
                label = { Text(task.status, fontSize = 11.sp) }
            )

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus Tugas",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onTaskAdded: (title: String, category: String, status: String, date: String) -> Unit
) {
    var titleInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tugas Utama") }
    var selectedStatus by remember { mutableStateOf("Dalam Proses") }
    var dateInput by remember { mutableStateOf("Hari Ini") }

    val categories = listOf("Tugas Utama", "Proyek", "Catatan")
    val statuses = listOf("Dalam Proses", "Mendatang", "Selesai")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Tugas Baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("Judul Tugas") },
                    placeholder = { Text("Contoh: Kerjakan Tugas Mobile") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Kategori:", style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = cat == selectedCategory,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 12.sp) }
                        )
                    }
                }

                Text("Status:", style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    statuses.forEach { st ->
                        FilterChip(
                            selected = st == selectedStatus,
                            onClick = { selectedStatus = st },
                            label = { Text(st, fontSize = 12.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = dateInput,
                    onValueChange = { dateInput = it },
                    label = { Text("Tenggat / Waktu") },
                    placeholder = { Text("Contoh: Besok, Hari Ini") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titleInput.isNotBlank()) {
                        onTaskAdded(titleInput, selectedCategory, selectedStatus, dateInput)
                    }
                },
                enabled = titleInput.isNotBlank()
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
