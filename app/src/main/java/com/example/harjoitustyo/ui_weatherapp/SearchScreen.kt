package com.example.harjoitustyo.ui_weatherapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.harjoitustyo.WeatherViewModel
import com.example.harjoitustyo.City

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: WeatherViewModel, navController: NavHostController) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {

        val focusRequester = remember { FocusRequester() }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    expanded = it.isNotEmpty()
                },
                label = { Text("City") },
                modifier = Modifier
                    .menuAnchor()        // tärkeää: sitoo valikon tähän TextFieldiin
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true
            )

            // Näytetään ehdotukset tekstikentän ALAPUOLELLA
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                viewModel._cities.filter { it.name.contains(query, ignoreCase = true) }
                    .forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city.name) },
                            onClick = {
                                viewModel.selectCity(city)
                                expanded = false
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }
            }
        }

        LaunchedEffect(key1 = true) {
            focusRequester.requestFocus()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kaupunkilista ja lisäyslomake (jätän ennalleen)
        viewModel._cities.forEach { city ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(city.name)
                Button(onClick = { viewModel.removeCity(city) }) {
                    Text("Remove")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        var newCityName by remember { mutableStateOf("") }
        var newLat by remember { mutableStateOf("") }
        var newLon by remember { mutableStateOf("") }

        Column {
            Spacer(Modifier.height(16.dp))
            Text("Add Custom City")

            TextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City Name") }
            )
            TextField(
                value = newLat,
                onValueChange = { newLat = it },
                label = { Text("Latitude") }
            )
            TextField(
                value = newLon,
                onValueChange = { newLon = it },
                label = { Text("Longitude") }
            )

            Button(onClick = {
                val lat = newLat.toDoubleOrNull()
                val lon = newLon.toDoubleOrNull()
                if (lat != null && lon != null && newCityName.isNotBlank()) {
                    val newCity = City(newCityName, lat, lon)
                    viewModel.addCity(newCity)
                    viewModel.selectCity(newCity)
                    query = ""
                    newCityName = ""
                    newLat = ""
                    newLon = ""
                    expanded = false
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }) {
                Text("Add City")
            }
        }
    }
}
