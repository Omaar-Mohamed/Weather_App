package com.example.weatherapp.model

import com.example.weatherapp.model.dto.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface AppRepo {
    suspend fun getWeather(lat: String, lon: String, apiKey: String): Flow<WeatherResponse>
}