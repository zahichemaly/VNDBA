package com.booboot.vndbandroid.util.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class LockableBottomSheetBehavior<V : View> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BottomSheetBehavior<V>(context, attrs) {
    var locked = false

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent) =
        if (!locked) super.onInterceptTouchEvent(parent, child, event)
        else false

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent) =
        if (!locked) super.onTouchEvent(parent, child, event)
        else false

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int) =
        if (!locked) super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
        else false

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (!locked) super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float) =
        if (!locked) super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
        else false
}