package com.booboot.vndbandroid.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndbandroid.Preferences

inline fun <reified A : Activity> Context.startActivity(configIntent: Intent.() -> Unit = {}) {
    startActivity(Intent(this, A::class.java).apply(configIntent))
}

fun Context.getBitmap(@DrawableRes drawableRes: Int): Bitmap? {
    return VectorDrawableCompat.create(resources, drawableRes, theme)?.let {
        val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
        bitmap
    }
}

fun Context.openURL(url: String) {
    if (Preferences.useCustomTabs) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(getThemeColor(R.attr.colorPrimary))
        getBitmap(R.drawable.ic_arrow_back_white_24dp)?.let {
            builder.setCloseButtonIcon(it)
        }
        builder.setStartAnimations(this, R.anim.slide_in, R.anim.slide_out)
        builder.setExitAnimations(this, R.anim.slide_back_in, R.anim.slide_back_out)
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    } else {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}

fun Context.getThemeColor(resid: Int): Int {
    val colorAttribute = TypedValue()
    theme.resolveAttribute(resid, colorAttribute, true)
    return colorAttribute.data
}

fun Context.getThemeColorState(@AttrRes resid: Int): Int {
    val data = getThemeColor(resid)
    val arr = obtainStyledAttributes(data, intArrayOf(resid))
    val primaryColor = arr.getColor(0, -1)
    arr.recycle()
    return primaryColor
}
