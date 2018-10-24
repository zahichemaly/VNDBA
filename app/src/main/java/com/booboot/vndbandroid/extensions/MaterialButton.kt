package com.booboot.vndbandroid.extensions

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.util.Pixels
import com.google.android.material.button.MaterialButton

fun MaterialButton.selectIf(select: Boolean) = if (select) select() else unselect()

fun MaterialButton.select() = apply {
    val textColorPrimary = context.getThemeColorState(android.R.attr.textColorPrimary)
    val newTextColor = if (strokeColor == textColorPrimary) {
        context.getThemeColorStateEnabled(android.R.attr.windowBackground)
    } else {
        ContextCompat.getColor(context, R.color.white)
    }

    setTextColor(newTextColor)
    iconTint = ColorStateList.valueOf(newTextColor)
    backgroundTintList = strokeColor
    elevation = Pixels.px(3).toFloat()
}

fun MaterialButton.unselect() = apply {
    setTextColor(strokeColor)
    iconTint = strokeColor
    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
    elevation = Pixels.px(0).toFloat()
}