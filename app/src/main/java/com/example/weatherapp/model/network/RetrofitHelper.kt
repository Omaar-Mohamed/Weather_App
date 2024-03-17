package com.example.weatherapp.model.network

import com.example.weatherapp.shared.ApiConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    val retrofitInstance= Retrofit.Builder()
        .baseUrl(ApiConstants.OPENWEATHERMAP_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}