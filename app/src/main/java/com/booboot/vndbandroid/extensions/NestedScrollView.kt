package com.booboot.vndbandroid.extensions

import android.view.View
import androidx.core.widget.NestedScrollView

fun NestedScrollView.scrollToTop() {
    fullScroll(View.FOCUS_UP)
    smoothScrollTo(0, 0)
}