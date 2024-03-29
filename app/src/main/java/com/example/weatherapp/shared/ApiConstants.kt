package com.example.weatherapp.shared

import android.content.Context
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
//        const val API_KEY = "172e0cbb3264b27530f5b6c425ffb29d"
        var lat: String ? = null
        var lon: String ? = null
        var address = "San Francisco"
        const val Google_API_KEY = "AIzaSyD3yjEwNflFdBz0N4Q-r4zkgQ6qiLcQBUs"
        var alertLocation = ""
        var alartlon = ""
        var alertlat = ""

        fun convertUnixTimestampToDateTime(unixTimestamp: Long): String {
            val date = Date(unixTimestamp * 1000L) // Convert seconds to milliseconds
            val sdf = SimpleDateFormat("hh a", Locale.getDefault()) // Define your desired date format with only hour and AM/PM indicator
            sdf.timeZone = TimeZone.getDefault() // Set the time zone (optional)
            return sdf.format(date) // Format the date
        }


        fun convertUnixTimestampToDateTimeToDailyAdapter(unixTimestamp: Long , language:String): String {
            val date = Date(unixTimestamp * 1000L) // Convert seconds to milliseconds
            val calendar = Calendar.getInstance()
            calendar.time = date
            val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> if (language == "ar") "الأحد" else "Sunday"
                Calendar.MONDAY -> if (language == "ar") "الإثنين" else "Monday"
                Calendar.TUESDAY -> if (language == "ar") "الثلاثاء" else "Tuesday"
                Calendar.WEDNESDAY -> if (language == "ar") "الأربعاء" else "Wednesday"
                Calendar.THURSDAY -> if (language == "ar") "الخميس" else "Thursday"
                Calendar.FRIDAY -> if (language == "ar") "الجمعة" else "Friday"
                Calendar.SATURDAY -> if (language == "ar") "السبت" else "Saturday"
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
        fun saveSelectedLanguage(context: Context, languageCode: String) {
            val sharedPref = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("selectedLanguage", languageCode)
                apply()
            }
        }

        fun getSelectedLanguage(context: Context): String {
            val sharedPref = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            return sharedPref.getString("selectedLanguage", "en") ?: "en" // Default to 'en' if not set
        }



    }
}