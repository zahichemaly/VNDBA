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

fun BottomSheetBehavior<*>.onStateChanged(
    onCollapsed: () -> Unit = {},
    onHidden: () -> Unit = {},
    onExpanded: () -> Unit = {},
    onExpanding: () -> Unit = {},
    onStateChanged: (Int) -> Unit = {}
) = object : BottomSheetBehavior.BottomSheetCallback() {
    override fun onSlide(bottomSheet: View, slideOffset: Float) {
    }

    @SuppressLint("SwitchIntDef")
    override fun onStateChanged(bottomSheet: View, newState: Int) {
        onStateChanged(newState)
        when (newState) {
            BottomSheetBehavior.STATE_COLLAPSED -> onCollapsed()
            BottomSheetBehavior.STATE_HIDDEN -> onHidden()
            BottomSheetBehavior.STATE_EXPANDED -> onExpanded()
            else -> onExpanding()
        }
    }
}.apply { setBottomSheetCallback(this) }