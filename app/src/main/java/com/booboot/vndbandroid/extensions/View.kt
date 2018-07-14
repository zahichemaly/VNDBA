package com.booboot.vndbandroid.extensions

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

fun View.toggle() {
    visibility = if (visibility == GONE) VISIBLE else GONE
}