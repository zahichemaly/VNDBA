package com.booboot.vndbandroid.extensions

import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.toggle(show: Boolean) = if (show) show() else hide()