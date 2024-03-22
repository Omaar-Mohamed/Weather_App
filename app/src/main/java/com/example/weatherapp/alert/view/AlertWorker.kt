package com.example.weatherapp.alert.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.R

class AlertWorker(appContext: Context, params: WorkerParameters) : Worker(appContext, params) {
    override fun doWork(): Result {
        // Create notification channels
        createNotificationChannels()

        // Create and display a notification
        showNotification()

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

    private fun showNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Define the notification content and attributes
        val channelId = "Your_Channel_ID"
        val notificationId = 123 // Unique ID for the notification
        val contentTitle = "Your Notification Title"
        val contentText = "Your Notification Text"
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
}
