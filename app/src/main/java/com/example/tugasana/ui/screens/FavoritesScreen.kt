package com.example.tugasana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tugasana.ui.TugasViewModel

data class FavoriteItem(
    val id: Int,
    val title: String,
    val description: String,
    val rating: String
)

@Composable
fun FavoritesScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: TugasViewModel
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedItemForDetail by remember { mutableStateOf<FavoriteItem?>(null) }

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

            // Title Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Favorit Saya",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Daftar komponen dan modul favorit yang telah disimpan (${viewModel.favorites.size} item).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.favorites.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada item favorit. Klik '+' untuk menambah.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = viewModel.favorites,
                        key = { it.id }
                    ) { item ->
                        FavoriteCard(
                            item = item,
                            onOpenDetail = { selectedItemForDetail = item },
                            onDelete = { viewModel.deleteFavorite(item.id) }
                        )
                    }
                }
            }
        }

        // FAB Add Favorite
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Tambah Favorit")
        }

        // Dialog Tambah Favorit
        if (showAddDialog) {
            AddFavoriteDialog(
                onDismiss = { showAddDialog = false },
                onFavoriteAdded = { title, desc, rating ->
                    viewModel.addFavorite(title, desc, rating)
                    showAddDialog = false
                }
            )
        }

        // Dialog Detail Favorit
        selectedItemForDetail?.let { item ->
            FavoriteDetailDialog(
                item = item,
                onDismiss = { selectedItemForDetail = null }
            )
        }
    }
}

@Composable
fun FavoriteCard(
    item: FavoriteItem,
    onOpenDetail: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB800),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.rating,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Favorit",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                TextButton(onClick = onOpenDetail) {
                    Text("Buka Detail")
                }
            }
        }
    }
}

@Composable
fun AddFavoriteDialog(
    onDismiss: () -> Unit,
    onFavoriteAdded: (title: String, desc: String, rating: String) -> Unit
) {
    var titleInput by remember { mutableStateOf("") }
    var descInput by remember { mutableStateOf("") }
    var ratingInput by remember { mutableStateOf("5.0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Favorit Baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("Judul Item") },
                    placeholder = { Text("Contoh: Jetpack Compose Material 3") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = descInput,
                    onValueChange = { descInput = it },
                    label = { Text("Deskripsi") },
                    placeholder = { Text("Penjelasan singkat...") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ratingInput,
                    onValueChange = { ratingInput = it },
                    label = { Text("Rating (1.0 - 5.0)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titleInput.isNotBlank()) {
                        onFavoriteAdded(titleInput, descInput, ratingInput)
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

@Composable
fun FavoriteDetailDialog(
    item: FavoriteItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB800))
                Spacer(modifier = Modifier.width(8.dp))
                Text(item.title)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Rating: ${item.rating} / 5.0",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}
