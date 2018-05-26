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
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.crashlytics.android.Crashlytics
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun setTitle(activity: Activity?, title: String) {
        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        actionBar?.title = title
    }

    fun openURL(context: Activity?, url: String) {
        if (context == null) return
        if (Preferences.useCustomTabs) {
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

    fun getThemeColor(context: Context?, resid: Int): Int {
        val colorAttribute = TypedValue()
        context?.theme?.resolveAttribute(resid, colorAttribute, true)
        return colorAttribute.data
    }

    fun processException(exception: Throwable) {
        if (BuildConfig.DEBUG) exception.printStackTrace()
        else Crashlytics.logException(exception)
    }

    fun getDate(date: String?, showFullDate: Boolean): String? = if (date == null) {
        "Unknown"
    } else try {
        val released = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        SimpleDateFormat(if (showFullDate) "d MMMM yyyy" else "yyyy", Locale.US).format(released)
    } catch (e: ParseException) {
        date
    }
}