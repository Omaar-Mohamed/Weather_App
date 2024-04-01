package com.example.weatherapp.Home.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AppRepo
import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.model.network.ApiState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val appRepo: AppRepo , private val sharedPreferences: SharedPreferences) : ViewModel() {

   private val _weather : MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val weather : StateFlow<ApiState> = _weather

    fun getWeather(lat: String, lon: String, apiKey: String , language:String , units: String) {
        viewModelScope.launch {
            appRepo.getWeather(lat, lon, apiKey , language , units)
                .catch { e ->
                    _weather.value = ApiState.Failure(e)
                }.collect {
                    _weather.value = ApiState.Success(it)
            }
        }
    }

    fun saveWeatherResponse(weatherResponse: WeatherResponse) {
        val gson = Gson()
        val json = gson.toJson(weatherResponse)
        sharedPreferences.edit().putString("weather_response", json).apply()
    }

    // Function to get weather response data from SharedPreferences
    fun getWeatherResponse(): WeatherResponse? {
        val gson = Gson()
        val json = sharedPreferences.getString("weather_response", null)
        return gson.fromJson(json, WeatherResponse::class.java)
    }
}