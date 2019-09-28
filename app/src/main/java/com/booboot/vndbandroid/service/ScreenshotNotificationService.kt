package com.booboot.vndbandroid.service

import android.app.IntentService
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.grantPermission
import com.booboot.vndbandroid.ui.slideshow.REQUEST_CODE_DELETE
import com.booboot.vndbandroid.ui.slideshow.REQUEST_CODE_SHARE
import com.booboot.vndbandroid.util.Notifications

class ScreenshotNotificationService : IntentService("ScreenshotNotificationService") {
    override fun onHandleIntent(intent: Intent?) {
        startForeground(SERVICE_ID, NotificationCompat.Builder(this, Notifications.DEFAULT_CHANNEL_ID).build())

        val action = intent?.getIntExtra(Intent.EXTRA_UID, -1)
        when (action) {
            REQUEST_CODE_SHARE -> {
                intent.component = null
                startActivity(Intent.createChooser(intent, getString(R.string.action_share)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }

            REQUEST_CODE_DELETE -> {
                val uri = intent.getParcelableExtra(Intent.EXTRA_ORIGINATING_URI) as? Uri
                uri?.let {
                    uri.grantPermission(this)
                    contentResolver.delete(uri, null, null)
                }
            }
        }

        val notificationManager = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(Notifications.IMAGE_DOWNLOADED_NOTIFICATION_ID)

        stopForeground(true)
        stopSelf()
    }

    companion object {
        const val SERVICE_ID = 52923
    }
}