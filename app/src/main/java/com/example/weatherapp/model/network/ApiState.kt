package com.example.weatherapp.model.network


import com.example.weatherapp.model.dto.WeatherResponse

sealed class ApiState {
    object Loading : ApiState()
    data class Success(val data: WeatherResponse) : ApiState()
    data class Failure(val error: Throwable) : ApiState()
}