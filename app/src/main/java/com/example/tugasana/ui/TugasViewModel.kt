package com.example.tugasana.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tugasana.ui.screens.FavoriteItem
import com.example.tugasana.ui.screens.TaskItem

class TugasViewModel : ViewModel() {

    // User Profile State
    var userName by mutableStateOf("Ana Roehana")
        private set

    var userEmail by mutableStateOf("ana.roehana@example.com")
        private set

    var userBio by mutableStateOf("Android Developer & Mahasiswa")
        private set

    var notificationsEnabled by mutableStateOf(true)
        private set

    var darkModeEnabled by mutableStateOf(false)
        private set

    // Task List State
    private val _tasks = mutableStateListOf(
        TaskItem(1, "Membuat Layout Mobile App", "Proyek", "Dalam Proses", "Hari Ini"),
        TaskItem(2, "Mendesain System Navigation", "Tugas Utama", "Selesai", "Kemarin"),
        TaskItem(3, "Persiapan Presentasi Tugas", "Catatan", "Mendatang", "Besok"),
        TaskItem(4, "Review Code Navigation Suite", "Proyek", "Dalam Proses", "Lusa")
    )
    val tasks: List<TaskItem> get() = _tasks

    // Favorites List State
    private val _favorites = mutableStateListOf(
        FavoriteItem(1, "Desain UI Material 3", "Kumpulan komponen UI Android terbaru dan modern.", "4.9"),
        FavoriteItem(2, "Modul Navigation Suite", "Modul navigasi adaptif untuk berbagai ukuran layar.", "4.8"),
        FavoriteItem(3, "Arsitektur Jetpack Compose", "Panduan best practice pengembangan aplikasi Compose.", "5.0"),
        FavoriteItem(4, "Pengelolaan State Compose", "Trik efisien mengelola stateful composables.", "4.7")
    )
    val favorites: List<FavoriteItem> get() = _favorites

    private var nextTaskId = 5
    private var nextFavoriteId = 5

    // --- Task Actions ---
    fun addTask(title: String, category: String, status: String, date: String) {
        if (title.isBlank()) return
        _tasks.add(0, TaskItem(nextTaskId++, title.trim(), category, status, date))
    }

    fun toggleTaskStatus(taskId: Int) {
        val index = _tasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val current = _tasks[index]
            val newStatus = if (current.status == "Selesai") "Dalam Proses" else "Selesai"
            _tasks[index] = current.copy(status = newStatus)
        }
    }

    fun deleteTask(taskId: Int) {
        _tasks.removeIf { it.id == taskId }
    }

    // --- Favorite Actions ---
    fun addFavorite(title: String, description: String, rating: String) {
        if (title.isBlank()) return
        _favorites.add(0, FavoriteItem(nextFavoriteId++, title.trim(), description.ifBlank { "Tidak ada deskripsi" }, rating.ifBlank { "5.0" }))
    }

    fun deleteFavorite(favoriteId: Int) {
        _favorites.removeIf { it.id == favoriteId }
    }

    // --- Profile Actions ---
    fun updateProfile(name: String, email: String, bio: String) {
        if (name.isNotBlank()) userName = name.trim()
        if (email.isNotBlank()) userEmail = email.trim()
        if (bio.isNotBlank()) userBio = bio.trim()
    }

    fun toggleNotifications(enabled: Boolean) {
        notificationsEnabled = enabled
    }

    fun toggleDarkMode(enabled: Boolean) {
        darkModeEnabled = enabled
    }
}
