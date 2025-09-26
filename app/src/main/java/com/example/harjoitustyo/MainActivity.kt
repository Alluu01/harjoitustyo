package com.example.harjoitustyo

import SearchScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.harjoitustyo.ui.theme.HarjoitustyoTheme
import com.example.harjoitustyo.ui_weatherapp.HomeScreen
import com.example.harjoitustyo.ui_weatherapp.SettingsScreen
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: WeatherViewModel = viewModel()
            MainScreen(viewModel)
        }
    }
}

@Composable
fun MainScreen(viewModel: WeatherViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(viewModel) }
            composable("search") { SearchScreen(viewModel, navController) }
            composable("settings") { SettingsScreen() }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf("home", "search", "settings")
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { /* Optional: add icons here */ },
                label = { Text(screen.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }) },
                selected = false, // optional: add state to highlight selected
                onClick = { navController.navigate(screen) }
            )
        }
    }
}