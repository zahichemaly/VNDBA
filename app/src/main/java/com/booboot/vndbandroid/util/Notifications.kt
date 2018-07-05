package com.booboot.vndbandroid.util

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.booboot.vndbandroid.R


object Notifications {
    const val DEFAULT_CHANNEL_ID = "DEFAULT_CHANNEL_ID"
    const val IMAGE_DOWNLOADED_NOTIFICATION_ID = 2002

    fun createNotificationChannels(application: Application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = application.getString(R.string.default_notification_channel_name)
            val description = application.getString(R.string.default_notification_channel_description)
            val channel = NotificationChannel(DEFAULT_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description

            val notificationManager = application.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}