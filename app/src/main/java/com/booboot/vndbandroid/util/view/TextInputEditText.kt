package com.booboot.vndbandroid.util.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.onSubmitListener
import com.booboot.vndbandroid.util.Pixels
import com.google.android.material.textfield.TextInputEditText

class TextInputEditText : TextInputEditText {
    /* Space between EditText and keyboard for better readability + space for error message */
    private var spaceAboveKeyboard: Int = 0

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) = attrs?.let {
        onSubmitListener { actionId ->
            if (actionId == IME_ACTION_DONE) clearFocus()
        }
        with(context.obtainStyledAttributes(attrs, R.styleable.TextInputEditText)) {
            if (!isInEditMode) {
                spaceAboveKeyboard = getDimensionPixelSize(R.styleable.TextInputEditText_spaceAboveKeyboard, Pixels.px(48))
            }
        }
    }

    override fun requestRectangleOnScreen(rect: Rect?): Boolean {
        rect?.let {
            rect.bottom += spaceAboveKeyboard
        }
        return super.requestRectangleOnScreen(rect)
    }
}