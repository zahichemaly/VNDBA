package com.booboot.vndbandroid.extensions

import android.annotation.SuppressLint
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun BottomSheetBehavior<*>.toggle() {
    val closedState = if (isHideable) BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_COLLAPSED
    state = when (state) {
        closedState -> BottomSheetBehavior.STATE_EXPANDED
        else -> closedState
    }
}

fun BottomSheetBehavior<*>.onStateChanged(onCollapsed: () -> Unit? = {}, onHidden: () -> Unit? = {}, onExpanded: () -> Unit? = {}) =
    object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        @SuppressLint("SwitchIntDef")
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> onCollapsed()
                BottomSheetBehavior.STATE_HIDDEN -> onHidden()
                else -> onExpanded()
            }
        }
    }.apply { setBottomSheetCallback(this) }