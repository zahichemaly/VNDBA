package com.booboot.vndbandroid.extensions

import android.annotation.SuppressLint
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun View.toggleBottomSheet() = BottomSheetBehavior.from(this).apply {
    state = when (state) {
        closedState() -> {
            /* To avoid multiple bottom sheets overlapping, closing other bottom sheets so only one is always open at once */
            /* /!\ Doesn't work when fast tapping the button to open bottom sheets: setState() doesn't work when a bottom sheet is already animating (accepted behavior) */
            (parent as? CoordinatorLayout)?.children?.forEach { child ->
                tryOrNull { BottomSheetBehavior.from(child) }?.let { childBehavior ->
                    childBehavior.state = childBehavior.closedState()
                }
            }

            BottomSheetBehavior.STATE_EXPANDED
        }
        else -> closedState()
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
}.apply { addBottomSheetCallback(this) }

fun BottomSheetBehavior<*>.isOpen() = state == BottomSheetBehavior.STATE_EXPANDED

fun BottomSheetBehavior<*>.closedState() = if (isHideable) BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_COLLAPSED