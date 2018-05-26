package com.booboot.vndbandroid.util

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.booboot.vndbandroid.App

object ErrorHandler {
    private var toast: Toast? = null

    fun showToast(stringId: Int) {
        showToast(App.context.getString(stringId))
    }

    fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            toast?.cancel()
            toast = Toast.makeText(App.context, message, if (message.length > 40) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
            toast?.show()
        }
    }
}