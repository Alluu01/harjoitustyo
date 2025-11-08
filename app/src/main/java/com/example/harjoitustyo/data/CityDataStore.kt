package com.example.harjoitustyo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.harjoitustyo.City
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CityDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("cities")
        private val CITIES_KEY = stringSetPreferencesKey("cities_key")
    }

    private val gson = Gson()

    val citiesFlow: Flow<List<City>> = context.dataStore.data.map {
        val jsonCities = it[CITIES_KEY] ?: emptySet()
        jsonCities.map { jsonCity -> gson.fromJson(jsonCity, City::class.java) }
    }

    suspend fun saveCities(cities: List<City>) {
        val jsonCities = cities.map { gson.toJson(it) }.toSet()
        context.dataStore.edit {
            it[CITIES_KEY] = jsonCities
        }
    }
}
