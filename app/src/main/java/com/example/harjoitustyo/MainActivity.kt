package com.example.harjoitustyo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.harjoitustyo.ui.theme.AppTheme
import com.example.harjoitustyo.ui_weatherapp.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            val viewModel: WeatherViewModel = viewModel()

            AppTheme(darkTheme = darkTheme) {
                MainScreen(
                    viewModel = viewModel,
                    darkTheme = darkTheme,
                    onToggleTheme = { darkTheme = !darkTheme }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: WeatherViewModel,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "welcome") {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") { WelcomeScreen(navController) }
            composable("home") { HomeScreen(viewModel) }
            composable("search") { SearchScreen(viewModel, navController) }
            composable("settings") {
                SettingsScreen(
                    darkTheme = darkTheme,
                    onToggleTheme = onToggleTheme
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", R.drawable.home_24px, "Home"),
        BottomNavItem("search", R.drawable.search_24px, "Search"),
        BottomNavItem("settings", R.drawable.settings_24px, "Settings")
    )

    NavigationBar(
        modifier = Modifier
            .height(72.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = item.iconRes),
                        contentDescription = item.label
                    )
                },
                selected = false,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val iconRes: Int,
    val label: String
)
