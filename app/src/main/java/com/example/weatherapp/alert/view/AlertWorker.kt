package com.example.weatherapp.alert.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.telecom.Call
import android.util.Log
import android.view.WindowInsetsAnimation
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.R
import com.example.weatherapp.model.AppRepoImpl
import com.example.weatherapp.model.db.AppLocalDataSourseImpL
import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.model.network.AppRemoteDataSourseImpl
import com.example.weatherapp.shared.ApiConstants
import com.google.android.gms.common.api.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext

class AlertWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        try {
            var lastIndex = inputData.getLong("lastIndex", 0)
            var lat = inputData.getString("alertLat")
            var lon = inputData.getString("alertLon")
            Log.i("lastIndexin", "doWork: ${lastIndex}")
            val weatherFlow = getCurrentWeather(applicationContext, lat, lon, ApiConstants.API_KEY)

            weatherFlow
                .catch { e ->
                    Result.failure()
                }
                .collect { weatherData ->

                    createNotificationChannels()

                    val x = inputData.getString("Weather Alert")
                    val y = inputData.getString("message")
                    if (weatherData.current.weather[0].main == WeatherState.VALUE1.value) {
                        showNotification(x, "weather in ${ApiConstants.splitTimeZone(weatherData.timezone)} is thunderstorm")
                    } else if (weatherData.current.weather[0].main == WeatherState.VALUE2.value) {
                        showNotification(x, "weather in ${ApiConstants.splitTimeZone(weatherData.timezone)} is mist")
                    } else if (weatherData.current.weather[0].main == WeatherState.VALUE3.value) {
                        showNotification(x, "weather in ${ApiConstants.splitTimeZone(weatherData.timezone)} is snow")
                    }else{
                        showNotification(x , "weather in ${ApiConstants.splitTimeZone(weatherData.timezone)} is clear")
                    }

//                    showNotification(x, ApiConstants.splitTimeZone(weatherData.timezone))
                    deleteAlertById(applicationContext, lastIndex)

                    Result.success()
                }
        } catch (e: Exception) {
            return Result.failure()
        }
//        showNotification("title", "message")
        return Result.success()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelId = "Your_Channel_ID"
            val channelName = "Your Channel Name"
            val channelDescription = "Your Channel Description"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = channelDescription

            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String?, message: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Define the notification content and attributes
        val channelId = "Your_Channel_ID"
        val notificationId = 123 // Unique ID for the notification
        val contentTitle = title
        val contentText = message
        val priority = NotificationCompat.PRIORITY_HIGH

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(priority)
            .setSmallIcon(R.drawable.weather)
        // Additional notification attributes can be set here

        // Display the notification
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private suspend fun getCurrentWeather(
        thisappContext: Context,
        lat: String?,
        lon: String?,
        apiKey: String
    ): Flow<WeatherResponse> {
        // Get the current weather data
        return AppRepoImpl.getInstance(
            AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(thisappContext)
        ).getWeather(lat ?: "", lon ?: "", apiKey)
    }

    private suspend fun deleteAlertById(thisappContext: Context, alertId: Long) {
        AppRepoImpl.getInstance(
            AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(thisappContext)
        ).deleteAlartById(alertId)
    }

    enum class WeatherState(val value: String) {
        VALUE1("thunderstorm"),
        VALUE2("mist"),
        VALUE3("snow"),
    }
}
