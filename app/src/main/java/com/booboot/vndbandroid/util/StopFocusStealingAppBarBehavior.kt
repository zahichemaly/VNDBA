package com.booboot.vndbandroid.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import com.google.android.material.appbar.AppBarLayout

internal class StopFocusStealingAppBarBehavior : AppBarLayout.Behavior {
    private var isAboveViewTouched = false
    private var aboveViews: Array<out View> = emptyArray()

    constructor(vararg aboveViews: View) {
        this.aboveViews = aboveViews
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onStartNestedScroll(parent: CoordinatorLayout, child: AppBarLayout, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        // Set flag if the bottom sheet is responsible for the nested scroll.
        isAboveViewTouched = target in aboveViews || target in aboveViews.flatMap {
            if (it is ViewGroup) it.children.toList() else listOf()
        }

        // Only consider starting a nested scroll if the bottom sheet is not touched; otherwise,
        // we will let the other views do the scrolling.
        return !isAboveViewTouched && super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: AppBarLayout, ev: MotionEvent): Boolean {
        // Don't accept touch stream here if the bottom sheet is touched. This will permit the
        // bottom sheet to be dragged down without interaction with the appBar. Reset on cancel.
        if (ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            isAboveViewTouched = false
        }
        return !isAboveViewTouched && super.onInterceptTouchEvent(parent, child, ev)
    }
}
