package com.booboot.vndbandroid.extensions

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

fun View.showKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    val showing = imm?.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    if (showing != true)
        scanForActivity()?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
}

fun EditText.setFocus() = post {
    requestFocus()
    isCursorVisible = true
    showKeyboard()
}

fun View.removeFocus() {
    clearFocus()
    hideKeyboard()
}

fun View.scanForActivity() = context?.scanForActivity()

fun View.setPaddingBottom(bottom: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, bottom)
}

fun View.addPaddingTop(top: Int) {
    setPadding(paddingLeft, paddingTop + top, paddingRight, paddingBottom)
}