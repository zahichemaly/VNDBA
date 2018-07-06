package com.booboot.vndbandroid.receiver

import android.app.IntentService
import android.app.NotificationManager
import android.content.Intent
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.grantPermission
import com.booboot.vndbandroid.ui.slideshow.SlideshowActivity
import com.booboot.vndbandroid.util.Notifications
import java.io.File


class ScreenshotNotificationService : IntentService("ScreenshotNotificationService") {
    override fun onHandleIntent(intent: Intent) {
        startForeground(SERVICE_ID, NotificationCompat.Builder(this, Notifications.DEFAULT_CHANNEL_ID).build())

        val action = intent.getIntExtra(Intent.EXTRA_UID, -1)
        when (action) {
            SlideshowActivity.REQUEST_CODE_SHARE -> {
                intent.component = null
                startActivity(Intent.createChooser(intent, getString(R.string.action_share)))
            }

            SlideshowActivity.REQUEST_CODE_DELETE -> {
                val path = intent.getStringExtra(Intent.EXTRA_TEXT)

                MediaScannerConnection.scanFile(applicationContext, arrayOf(path), null) { _, uri ->
                    uri?.grantPermission(this)
                    contentResolver.delete(uri, null, null)
                    File(path).delete()
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