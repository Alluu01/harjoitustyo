package com.example.harjoitustyo.ui_weatherapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.harjoitustyo.WeatherViewModel


@Composable
fun HomeScreen(viewModel: WeatherViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(viewModel.selectedCity.value, style = MaterialTheme.typography.headlineMedium)
        // Add your weather cards or icons here (currently hard-codddded)
    }
}
