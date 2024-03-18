package com.example.weatherapp.model

import com.example.weatherapp.model.dto.FavLocations
import com.example.weatherapp.model.dto.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface AppRepo {
    suspend fun getWeather(lat: String, lon: String, apiKey: String): Flow<WeatherResponse>

    suspend fun getAllLocations(): Flow<List<FavLocations>>
    suspend fun insertLocation(location: FavLocations): Long
    suspend fun deleteLocation(location: FavLocations)
}