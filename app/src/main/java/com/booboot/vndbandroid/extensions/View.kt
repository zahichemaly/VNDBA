package com.booboot.vndbandroid.extensions

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun View.toggle() {
    isVisible = visibility == GONE
}

fun View.toggle(show: Boolean) = when (this) {
    is FloatingActionButton -> toggle(show)
    is ExtendedFloatingActionButton -> toggle(show)
    else -> isVisible = show
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}