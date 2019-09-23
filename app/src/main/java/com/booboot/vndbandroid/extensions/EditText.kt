package com.booboot.vndbandroid.extensions

import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showCustomError(message: String, errorView: TextView) = apply {
    error = " "
    errorView.text = message
    errorView.isVisible = true
}

fun TextInputLayout.hideCustomError(errorView: TextView) = apply {
    error = null
    errorView.isVisible = false
}