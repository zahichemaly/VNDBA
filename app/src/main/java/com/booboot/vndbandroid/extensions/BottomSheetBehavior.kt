package com.booboot.vndbandroid.extensions

import android.annotation.SuppressLint
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun BottomSheetBehavior<*>.toggle() {
    state = when (state) {
        BottomSheetBehavior.STATE_COLLAPSED -> BottomSheetBehavior.STATE_EXPANDED
        else -> BottomSheetBehavior.STATE_COLLAPSED
    }
}

fun BottomSheetBehavior<*>.onStateChanged(onCollapsed: () -> Unit, onExpanded: () -> Unit) =
    setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        @SuppressLint("SwitchIntDef")
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> onCollapsed()
                else -> onExpanded()
            }
        }
    })