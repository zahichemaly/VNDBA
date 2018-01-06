package com.booboot.vndbandroid.util

import android.app.Activity
import android.support.v7.app.AppCompatActivity

object Utils {
    fun setTitle(activity: Activity, title: String) {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        if (actionBar != null)
            actionBar.title = title
    }
}