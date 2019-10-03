package com.booboot.vndbandroid.extensions

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
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
    requestFocusFromTouch()
    isCursorVisible = true
    showKeyboard()
}

fun View.removeFocus() {
    clearFocus()
    hideKeyboard()
}

fun View.scanForActivity() = context?.scanForActivity()

/**
 * Defines bounds of displayed view and check is it contains [Point]
 * @param view View to define bounds
 * @param point Point to check inside bounds
 * @return `true` if view bounds contains point, `false` - otherwise
 */
fun View.isPointInsideBounds(point: Point): Boolean = Rect().run {
    // get view rectangle
    getDrawingRect(this)

    // apply offset
    IntArray(2).also { locationOnScreen ->
        getLocationOnScreen(locationOnScreen)
        offset(locationOnScreen[0], locationOnScreen[1])
    }

    // check is rectangle contains point
    contains(point.x, point.y)
}

fun View.setPaddingBottom(bottom: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, bottom)
}