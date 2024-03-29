package com.example.weatherapp.Home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AppRepo
import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.model.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val appRepo: AppRepo) : ViewModel() {

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
}