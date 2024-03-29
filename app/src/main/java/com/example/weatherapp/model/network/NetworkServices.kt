package com.example.weatherapp.model.network

import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.shared.ApiConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkServices {
    @GET(ApiConstants.FORECAST_ENDPOINT)
    suspend fun getWeather(
        @Query("lat") lan: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String,
        @Query("lang") language: String,
        @Query("units") units: String
    ): WeatherResponse
}