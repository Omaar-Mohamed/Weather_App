package com.example.weatherapp.shared

import java.text.SimpleDateFormat
import java.util.Calendar
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
            val sdf = SimpleDateFormat("hh a", Locale.getDefault()) // Define your desired date format with only hour and AM/PM indicator
            sdf.timeZone = TimeZone.getDefault() // Set the time zone (optional)
            return sdf.format(date) // Format the date
        }


        fun convertUnixTimestampToDateTimeToDailyAdapter(unixTimestamp: Long): String {
            val date = Date(unixTimestamp * 1000L) // Convert seconds to milliseconds
            val calendar = Calendar.getInstance()
            calendar.time = date
            val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> "Sunday"
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                else -> ""
            }
            return dayOfWeek
        }



        fun getWeatherIconUrl(icon: String): String {
            return "https://openweathermap.org/img/wn/$icon@2x.png"
        }

        fun splitTimeZone(timezone: String) : String{

            val parts = timezone.split("/")
            if (parts.size == 2) {
                val country = parts[0].trim()
                val city = parts[1].trim()
                // Now you can use `country` and `city` as needed
                return "$country, $city"
            } else {
                return ""
            }
        }

        fun kelvinToCelsius(kelvin: Double): String {
            val celsius = kelvin - 273.15
            return String.format("%.1f°C", celsius)
        }
        fun kelvinToCelsiusToAdapter(kelvin: Double): String {
            val celsius = kelvin - 273.15
            return String.format("%.0f°C", celsius)
        }
    }
}