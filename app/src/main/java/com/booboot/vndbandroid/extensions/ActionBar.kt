package com.booboot.vndbandroid.extensions

import androidx.appcompat.app.ActionBar

fun ActionBar.toggle() = if (isShowing) hide() else show()