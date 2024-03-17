package com.example.weatherapp.shared

class ApiConstants {
    companion object {
        const val OPENWEATHERMAP_BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val FORECAST_ENDPOINT = "forecast"
        const val API_KEY = "ec44d57c65496e30a27e3c71bac5eaff"
        var lat: String ? = null
        var lon: String ? = null
        var address = "San Francisco"
    }
}