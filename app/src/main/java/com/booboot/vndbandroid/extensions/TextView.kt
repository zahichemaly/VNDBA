package com.booboot.vndbandroid.extensions

import android.widget.TextView

fun TextView.preventLineBreak(_maxLines: Int = Int.MAX_VALUE) = apply {
    setSingleLine()
    setHorizontallyScrolling(false)
    maxLines = _maxLines
}