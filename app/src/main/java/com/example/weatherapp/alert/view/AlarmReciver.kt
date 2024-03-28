package com.example.weatherapp.alert.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.ListenableWorker
import com.example.weatherapp.R
import com.example.weatherapp.model.AppRepoImpl
import com.example.weatherapp.model.db.AppLocalDataSourseImpL
import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.model.network.AppRemoteDataSourseImpl
import com.example.weatherapp.shared.ApiConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlarmReciver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {

            var lastIndex = intent.getLongExtra("id", 0)
            var lat = intent.getStringExtra("lat")
            var lon = intent.getStringExtra("long")
            Log.i("lastIndexin", "doWork: ${lastIndex}")
            CoroutineScope(Dispatchers.IO).launch {
                val weatherFlow = getCurrentWeather(context,lat,lon ,  ApiConstants.API_KEY)

                weatherFlow
                .catch {
                        e ->
                    ListenableWorker.Result.failure()
                }
                .collect { weatherData ->

                    createNotificationChannels(context)

                    val x = "title"
//                    val y = inputData.getString("message")


                    showNotification(x, ApiConstants.splitTimeZone(weatherData.timezone) , context)
                    deleteAlertById(context, lastIndex)

                }
        }
        } catch (e: Exception) {
        }
//        showNotification("title", "message")
    }


    private fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

    private fun showNotification( title: String?, message: String? , context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Define the notification content and attributes
        val channelId = "Your_Channel_ID"
        val notificationId = 123 // Unique ID for the notification
        val contentTitle = title
        val contentText = message
        val priority = NotificationCompat.PRIORITY_HIGH

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(priority)
            .setSmallIcon(R.drawable.weather)
        // Additional notification attributes can be set here

        // Display the notification
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private suspend fun getCurrentWeather(thisappContext: Context, lat: String?, lon: String?, apiKey: String): Flow<WeatherResponse> {
        // Get the current weather data
        return AppRepoImpl.getInstance(
            AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(thisappContext)
        ).getWeather(lat ?: "", lon ?: "", apiKey , "en")
    }

    private suspend fun deleteAlertById(thisappContext: Context, alertId: Long) {
        AppRepoImpl.getInstance(
            AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(thisappContext)
        ).deleteAlartById(alertId)
    }
}