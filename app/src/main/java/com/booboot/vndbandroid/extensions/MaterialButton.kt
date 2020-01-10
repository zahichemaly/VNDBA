package com.booboot.vndbandroid.extensions

import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.util.Pixels
import com.google.android.material.button.MaterialButton

fun MaterialButton.selectIf(
    select: Boolean,
    @ColorRes textColor: Int = R.color.white
) = if (select) select(textColor) else unselect()

fun MaterialButton.select(
    @ColorRes textColor: Int = R.color.white
) = apply {
    val newTextColor = ContextCompat.getColor(context, textColor)
    setTextColor(newTextColor)
    iconTint = ColorStateList.valueOf(newTextColor)
    backgroundTintList = strokeColor
    elevation = Pixels.px(3).toFloat()
    rippleColor = textColors
}

fun MaterialButton.unselect() = apply {
    setTextColor(strokeColor)
    iconTint = strokeColor
    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
    elevation = Pixels.px(0).toFloat()
    rippleColor = textColors
}