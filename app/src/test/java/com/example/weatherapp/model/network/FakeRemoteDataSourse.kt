package com.example.weatherapp.model.network

import com.example.weatherapp.model.dto.WeatherResponse
import kotlinx.coroutines.flow.Flow

class FakeRemoteDataSourse: AppRemoteDataSourse{
    override suspend fun getWeather(
        lat: String,
        lon: String,
        apiKey: String,
        language: String,
        units: String
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }
}