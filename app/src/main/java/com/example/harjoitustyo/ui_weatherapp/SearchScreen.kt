package com.example.harjoitustyo.ui_weatherapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.harjoitustyo.WeatherViewModel
import com.example.harjoitustyo.City
import com.example.harjoitustyo.R
import com.example.harjoitustyo.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: WeatherViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text("My Cities", style = MaterialTheme.typography.displaySmall)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 56.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("City", style = MaterialTheme.typography.bodyMedium)
            Text("Select", style = MaterialTheme.typography.bodyMedium)
            Text("Remove", style = MaterialTheme.typography.bodyMedium)
        }

        // 🏙️ Kaupunkilista (scrollaa nyt)
        LazyColumn(
            modifier = Modifier
                .weight(1f)   // 🔑 ottaa kaiken jäljellä olevan tilan -> rajaa korkeuden
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel._cities.size) { index ->
                val city = viewModel._cities[index]

                Card(
                    modifier = Modifier.padding(horizontal = 40.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            city.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                viewModel.selectCity(city)
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .width(115.dp)
                                .padding(end = 35.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            val isDark = isSystemInDarkTheme()
                            val icon = if (isDark) R.drawable.check_dark else R.drawable.check_light

                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = "Select",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            )
                        }



                        Button(
                            onClick = { viewModel.removeCity(city) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            val isDark = isSystemInDarkTheme()
                            val icon =
                                if (isDark) R.drawable.remove_dark else R.drawable.remove_light


                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = "Remove",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(0.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(bottom = 70.dp),

            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {

            var newCityName by remember { mutableStateOf("") }
            var newLat by remember { mutableStateOf("") }
            var newLon by remember { mutableStateOf("") }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.height(20.dp))
                Text("Add City", style = MaterialTheme.typography.bodyMedium)

                TextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City Name", style = MaterialTheme.typography.bodyMedium) })
                TextField(
                    value = newLat,
                    onValueChange = { newLat = it },
                    label = { Text("Latitude", style = MaterialTheme.typography.bodyMedium) })
                TextField(
                    value = newLon,
                    onValueChange = { newLon = it },
                    label = { Text("Longitude", style = MaterialTheme.typography.bodyMedium) })

                Button(
                    onClick = {
                        val lat = newLat.toDoubleOrNull()
                        val lon = newLon.toDoubleOrNull()
                        if (lat != null && lon != null && newCityName.isNotBlank()) {
                            val newCity = City(newCityName, lat, lon)
                            viewModel.addCity(newCity)
                            viewModel.selectCity(newCity)
                            newCityName = ""
                            newLat = ""
                            newLon = ""
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    val isDark = isSystemInDarkTheme()
                    val icon = if (isDark) R.drawable.add_light else R.drawable.add_dark

                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )

                }
            }
        }
    }
}
