package com.booboot.vndbandroid.util.view

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout

class FrameLayoutKeyboardSupport : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        /* removes the paddings induced by fitsSystemWindows="true" on all children views */
        setPadding(0, 0, 0, insets.systemWindowInsetBottom)
        return insets.consumeSystemWindowInsets()
    }
}