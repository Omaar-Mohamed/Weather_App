package com.example.weatherapp.model.network

import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.model.network.RetrofitHelper.retrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object  AppRemoteDataSourseImpl:AppRemoteDataSourse {
    override suspend fun getWeather(lat: String, lon: String, apiKey: String): Flow<WeatherResponse> = flow {
        val weather = retrofitInstance.create(NetworkServices::class.java).getWeather(lat, lon, apiKey)
        emit(weather)


    }

}