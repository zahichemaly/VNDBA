package com.booboot.vndbandroid.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import com.booboot.vndbandroid.BuildConfig
import com.booboot.vndbandroid.R
import com.crashlytics.android.Crashlytics

object Utils {
    fun setTitle(activity: Activity, title: String) {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        if (actionBar != null)
            actionBar.title = title
    }

    fun openURL(context: Activity?, url: String) {
        if (context == null) return
        if (PreferencesManager.useCustomTabs()) {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(getThemeColor(context, R.attr.colorPrimary))
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_arrow_back_white_24dp))
            builder.setStartAnimations(context, R.anim.slide_in, R.anim.slide_out)
            builder.setExitAnimations(context, R.anim.slide_back_in, R.anim.slide_back_out)
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
    }

    fun getThemeColor(context: Context, resid: Int): Int {
        val colorAttribute = TypedValue()
        context.theme.resolveAttribute(resid, colorAttribute, true)
        return colorAttribute.data
    }

    fun processException(exception: Throwable) {
        if (BuildConfig.DEBUG) exception.printStackTrace()
        else Crashlytics.logException(exception)
    }
}