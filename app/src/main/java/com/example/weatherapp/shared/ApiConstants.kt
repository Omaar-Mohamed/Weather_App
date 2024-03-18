package com.example.weatherapp.shared

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ApiConstants {
    companion object {
        const val OPENWEATHERMAP_BASE_URL = "https://api.openweathermap.org/data/3.0/"
        const val FORECAST_ENDPOINT = "onecall"
        const val API_KEY = "ec44d57c65496e30a27e3c71bac5eaff"
        var lat: String ? = null
        var lon: String ? = null
        var address = "San Francisco"
        const val Google_API_KEY = "AIzaSyD3yjEwNflFdBz0N4Q-r4zkgQ6qiLcQBUs"

        fun convertUnixTimestampToDateTime(unixTimestamp: Long): String {
            val date = Date(unixTimestamp * 1000L) // Convert seconds to milliseconds
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Define your desired date format
            sdf.timeZone = TimeZone.getDefault() // Set the time zone (optional)
            return sdf.format(date) // Format the date
        }

        fun getWeatherIconUrl(icon: String): String {
            return "https://openweathermap.org/img/wn/$icon@2x.png"
        }
    }
}