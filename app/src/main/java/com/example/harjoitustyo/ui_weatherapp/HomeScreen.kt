package com.example.harjoitustyo.ui_weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.harjoitustyo.R
import com.example.harjoitustyo.WeatherViewModel


@Composable
fun HomeScreen(viewModel: WeatherViewModel) {
    Column(modifier = Modifier.padding(32.dp)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(viewModel.selectedCity.value, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.clear_day),
                contentDescription = "Cloudy",
                modifier = Modifier
                    .size(250.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text("21 °C", style = MaterialTheme.typography.displayLarge)
        }
        LazyColumn {
            items(55) { item ->
                Text("Item $item", fontSize = 24.sp)
            }
        }
    }
}
