package com.booboot.vndbandroid.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.booboot.vndbandroid.R
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

fun EditText.setTextChangedListener(onTextChanged: (String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            text?.let { onTextChanged(it.toString()) }
        }
    }

    removeTextChangedListener()
    setTag(R.id.tagTextWatcher, textWatcher)
    addTextChangedListener(textWatcher)

    return textWatcher
}

fun EditText.removeTextChangedListener() {
    val oldTextWatcher = getTag(R.id.tagTextWatcher) as? TextWatcher
    removeTextChangedListener(oldTextWatcher)
}