package com.booboot.vndbandroid.extensions

import com.google.android.material.bottomsheet.BottomSheetBehavior

fun BottomSheetBehavior<*>.toggle() {
    state = when (state) {
        BottomSheetBehavior.STATE_COLLAPSED -> BottomSheetBehavior.STATE_EXPANDED
        else -> BottomSheetBehavior.STATE_COLLAPSED
    }
}