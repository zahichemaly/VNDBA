package com.booboot.vndbandroid.extensions

import android.graphics.Color
import androidx.annotation.ColorInt
import kotlin.math.roundToInt

/**
 * Generates a unique Int with two Ints, based on the Cantor pairing function.
 * See https://stackoverflow.com/questions/919612/mapping-two-integers-to-one-in-a-unique-and-deterministic-way
 */
fun Long.generateUnique(b: Long) = (this + b) * (this + b + 1) / 2 + b

@ColorInt
fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt()
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)
    return Color.argb(alpha, red, green, blue)
}

@ColorInt
fun Int.darken(): Int {
    val hsv = floatArrayOf(0f, 0f, 0f)
    Color.colorToHSV(this, hsv)
    hsv[2] -= 0.25f
    if (hsv[2] < 0) hsv[2] = 0f
    return Color.HSVToColor(hsv)
}