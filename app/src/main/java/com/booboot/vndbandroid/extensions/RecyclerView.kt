package com.booboot.vndbandroid.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.util.view.LockableBottomSheetBehavior

fun <T : View> RecyclerView.hideOnBottom(fab: T?) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                val visibleItemCount = recyclerView.layoutManager?.childCount ?: 0
                val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                val pastVisiblesItems = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                fab?.toggle(visibleItemCount + pastVisiblesItems < totalItemCount)
            } else {
                fab?.toggle(true)
            }
        }
    })
}

@SuppressLint("ClickableViewAccessibility")
fun RecyclerView.fixScrollWithBottomSheet(bottomSheetBehavior: LockableBottomSheetBehavior<*>?) = setOnTouchListener { _, event ->
    when (event.action) {
        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
            if (computeVerticalScrollOffset() > 0) bottomSheetBehavior?.locked = true
        }
        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
            bottomSheetBehavior?.locked = false
        }
    }
    false
}