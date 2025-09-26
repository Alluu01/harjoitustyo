package com.example.harjoitustyo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {
    // List of cities
    val cities = mutableStateListOf("Tampere", "Helsinki", "Oulu", "Turku", "Espoo")

    // Selected city
    var selectedCity = mutableStateOf(cities.first())
}
