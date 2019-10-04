package com.booboot.vndbandroid.extensions

import com.booboot.vndbandroid.util.Pixels
import com.google.android.material.chip.Chip

fun Chip.toggleText(value: String) {
    text = if (text.isNullOrEmpty()) value else null
    setPadding()
}

fun Chip.setPadding() = if (text.isNullOrEmpty()) {
    chipEndPadding = 0f
    textStartPadding = 0f
    textEndPadding = 0f
    chipStartPadding = 0f
    iconEndPadding = 0f
    iconStartPadding = 0f
    closeIconStartPadding = 0f
    closeIconEndPadding = 0f
    setPadding(0, 0, 0, 0)
} else {
    chipEndPadding = Pixels.px(6).toFloat()
    textStartPadding = Pixels.px(8).toFloat()
    textEndPadding = Pixels.px(6).toFloat()
}