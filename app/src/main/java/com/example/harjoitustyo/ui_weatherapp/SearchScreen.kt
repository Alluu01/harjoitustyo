import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.harjoitustyo.WeatherViewModel


@Composable
fun SearchScreen(viewModel: WeatherViewModel, navController: NavHostController) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Search bar
        Column {
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    expanded = it.isNotEmpty()
                },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                viewModel.cities.filter { it.contains(query, ignoreCase = true) }
                    .forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                query = city
                                expanded = false
                                viewModel.selectedCity.value = city
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of cities with add/remove buttons
        viewModel.cities.forEach { city ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(city)
                Button(onClick = { viewModel.cities.remove(city) }) {
                    Text("Remove")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.cities.add("NewCity") }) {
            Text("Add City")
        }
    }
}

@Composable //todo//
fun CitySearchBar(
    allCities: List<String>,
    onCitySelected: (String) -> Unit,
    selectedCity: String,
    modifier: Modifier
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = query,
            onValueChange = {
                query = it
                expanded = it.isNotEmpty()
            },
            label = { Text("Select a city") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            allCities.filter { it.contains(query, ignoreCase = true) }.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        query = city
                        expanded = false
                        onCitySelected(city)
                    }
                )
            }
        }
    }
}