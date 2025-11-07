package com.example.harjoitustyo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class City(val name: String, val latitude: Double, val longitude: Double)

@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel : ViewModel() {

    // List of cities with coordinates
    val _cities = mutableStateListOf<City>(
        City("Tampere", 61.4991, 23.7871),
        City("Helsinki", 60.1695, 24.9354),
        City("Turku", 60.4518, 22.2666),
        City("Oulu", 65.0121, 25.4651),
        City("Espoo", 60.2055, 24.6559)
    )

    private val _selectedCity = MutableStateFlow<City>(_cities.first())
    val selectedCity: StateFlow<City> = _selectedCity.asStateFlow()


    fun updateSelectedCity(newCity: City) {
        _selectedCity.value = newCity
    }

    // Temperatures for next 24 hours
    private val _temperatures = MutableStateFlow<List<Double>>(emptyList())
    val temperatures: StateFlow<List<Double>> = _temperatures

    // Weather codes for next 24 hours
    private val _weatherCodes = MutableStateFlow<List<Int>>(emptyList())
    val weatherCodes: StateFlow<List<Int>> = _weatherCodes

    // Retrieve times for next 24 hours
    private val _times = MutableStateFlow<List<String>>(emptyList())
    val times: StateFlow<List<String>> = _times

    private val _rain = MutableStateFlow<List<Double>>(emptyList())
    val rain: StateFlow<List<Double>> = _rain

    // Current weather icon
    private val _currentIcon = MutableStateFlow(R.drawable.missing_data)
    val currentIcon: StateFlow<Int> = _currentIcon


    fun selectCity(city: City) {
        _selectedCity.value = city
    }

    fun addCity(city: City) {
        if (!_cities.any { it.name.equals(city.name, true) }) {
            _cities.add(city)
        }
    }

    fun removeCity(city: City) {
        _cities.remove(city)
        if (_selectedCity.value == city && _cities.isNotEmpty()) {
            selectCity(_cities.first())
        }
    }

    init {
        viewModelScope.launch {
            selectedCity.collect { city ->
                city.let { fetchWeather(it) }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchWeather(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url =
                    "https://api.open-meteo.com/v1/forecast?latitude=${city.latitude}&longitude=${city.longitude}&hourly=temperature_2m,weather_code,rain&timezone=Europe/Helsinki&forecast_days=2"
                val data = URL(url).readText()
                val json = JSONObject(data)
                val hourly = json.getJSONObject("hourly")

                val tempsJson = hourly.getJSONArray("temperature_2m")
                val codesJson = hourly.getJSONArray("weather_code")
                val timesJson = hourly.getJSONArray("time")
                val rainJson = hourly.getJSONArray("rain")

                // Listat kaikista tunneista (esim. 48h)
                val temps = List(tempsJson.length()) { i -> tempsJson.getDouble(i) }
                val codes = List(codesJson.length()) { i -> codesJson.getInt(i) }
                val rainAmounts = List(rainJson.length()) { i -> rainJson.getDouble(i) }
                val times = List(timesJson.length()) { i ->
                    timesJson.getString(i).substring(11, 16) // HH:mm
                }

                val totalHours = 24
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                val now = LocalDateTime.now()

                // Parsitaan ajat LocalDateTime-muotoon
                val parsedTimes = List(timesJson.length()) { i ->
                    LocalDateTime.parse(timesJson.getString(i), formatter)
                }

                // Etsi ensimmäinen aika, joka on nyt tai tulevaisuudessa
                val startIndex = parsedTimes.indexOfFirst { !it.isBefore(now) }.let {
                    if (it == 1) 0 else it
                }

                // Jos dataa ei ole tarpeeksi, täydennetään alusta
                val availableHours = parsedTimes.size - startIndex
                val remaining = totalHours - availableHours

                val fullTemps = if (availableHours >= totalHours) {
                    temps.subList(startIndex, startIndex + totalHours)
                } else {
                    temps.subList(startIndex, temps.size) + temps.subList(0, remaining)
                }

                val fullCodes = if (availableHours >= totalHours) {
                    codes.subList(startIndex, startIndex + totalHours)
                } else {
                    codes.subList(startIndex, codes.size) + codes.subList(0, remaining)
                }

                val fullRain = if (availableHours >= totalHours) {
                    rainAmounts.subList(startIndex, startIndex + totalHours)
                } else {
                    rainAmounts.subList(startIndex, rainAmounts.size) + rainAmounts.subList(
                        0, remaining
                    )
                }

                val fullTimes = if (availableHours >= totalHours) {
                    parsedTimes.subList(startIndex, startIndex + totalHours)
                } else {
                    parsedTimes.subList(startIndex, parsedTimes.size) + parsedTimes.subList(
                        0, remaining
                    )
                }


                // Muunna ajat näyttömuotoon (esim. "HH:mm")
                val displayTimes = fullTimes.map { it.format(DateTimeFormatter.ofPattern("HH:mm")) }

                // Aseta arvot
                _temperatures.value = fullTemps
                _weatherCodes.value = fullCodes
                _rain.value = fullRain
                _times.value = displayTimes

                Log.d("WeatherCodes", fullCodes.take(25).joinToString(", "))




                // Käytä nykyisen tunnin iconia
                // Käytä nykyisen tunnin iconia
                val icon = getWeatherIcon(_weatherCodes.value.firstOrNull() ?: 0)
                withContext(Dispatchers.Main) {
                    _currentIcon.value = icon
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun getWeatherIcon(code: Int): Int {
        return when (code) {
            0, 1 -> R.drawable.sunny
            2 -> R.drawable.partly_cloudy
            3, 4 -> R.drawable.cloudy
            5, 6, 20, 21 -> R.drawable.rain_sunny
            7, 8 -> R.drawable.rain_heavy
            10, 11, 90, 91 -> R.drawable.snow_light
            12, 13, 92, 93 -> R.drawable.snow_heavy
            15, 16, 17, 95, 96, 97 -> R.drawable.sleet
            in 25..39 -> R.drawable.fog
            in 80..84 -> R.drawable.thunderstorm
            18, 19, 22, 23, 94, 98 -> R.drawable.mixed_precip
            61, 51, 53, 55 -> R.drawable.rain_light
            else -> R.drawable.missing_data
        }
    }
}

