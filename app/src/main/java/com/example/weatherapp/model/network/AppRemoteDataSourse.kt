package com.example.weatherapp.model.network

import com.example.weatherapp.model.dto.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface AppRemoteDataSourse {
    suspend fun getWeather(lat: String, lon: String, apiKey: String): Flow<WeatherResponse>
}