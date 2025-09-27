package com.example.harjoitustyo.ui_weatherapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.harjoitustyo.WeatherViewModel
import com.example.harjoitustyo.R


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: WeatherViewModel = viewModel()) {
    val temps by viewModel.temperatures.collectAsState()
    val city by viewModel.selectedCity.collectAsState()
    val times by viewModel.times.collectAsState()
    val rain by viewModel.rain.collectAsState()
    val currentIcon by viewModel.currentIcon.collectAsState()


    Column(modifier = Modifier.padding(24.dp)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(city.name, style = MaterialTheme.typography.displayMedium)
            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = currentIcon),
                contentDescription = "Current Weather",
                modifier = Modifier.size(200.dp)
            )

            // Display current temperature if available
            val currentTemp = temps.firstOrNull() ?: 0.0
            Text("${currentTemp.toInt()} °C", style = MaterialTheme.typography.displayLarge)

            Spacer(modifier = Modifier.height(72.dp))

            NumberListScreen(temps, times, rain) // hand off temperature, time and rain to NumberListScreen
        }
    }
}

@Composable
fun NumberListScreen(temps: List<Double>, times: List<String>, rain: List<Double>) {

    if (temps.isEmpty() || times.isEmpty()) {
        Text("Loading weather data...")
        return
    }

    // Combine times, temps, and rain into a list of Triple
    val items =
        times.zip(temps).zip(rain) { (time, temp), rainAmount -> Triple(time, temp, rainAmount) }


    Column(modifier = Modifier.fillMaxWidth()) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Time", style = MaterialTheme.typography.bodyMedium)
            Text("Temperature", style = MaterialTheme.typography.bodyMedium)
            Text("Rain amount", style = MaterialTheme.typography.bodyMedium)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(items) { (time, temp, rainAmount) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(time, style = MaterialTheme.typography.bodyMedium)
                        Text("${temp.toInt()}°C", style = MaterialTheme.typography.bodyMedium)
                        Text("${rainAmount}mm", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
