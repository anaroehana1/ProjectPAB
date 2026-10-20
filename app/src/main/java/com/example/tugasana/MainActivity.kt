package com.example.tugasana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tugasana.ui.TugasViewModel
import com.example.tugasana.ui.screens.FavoritesScreen
import com.example.tugasana.ui.screens.HomeScreen
import com.example.tugasana.ui.screens.ProfileScreen
import com.example.tugasana.ui.theme.TUGASANATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TUGASANATheme {
                TUGASANAApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun TUGASANAApp(viewModel: TugasViewModel = viewModel()) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen(innerPadding, viewModel)
                AppDestinations.FAVORITES -> FavoritesScreen(innerPadding, viewModel)
                AppDestinations.PROFILE -> ProfileScreen(innerPadding, viewModel)
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Beranda", R.drawable.ic_home),
    FAVORITES("Favorit", R.drawable.ic_favorite),
    PROFILE("Profil", R.drawable.ic_account_box),
}

@Preview(showBackground = true)
@Composable
fun TUGASANAAppPreview() {
    TUGASANATheme {
        TUGASANAApp()
    }
}
