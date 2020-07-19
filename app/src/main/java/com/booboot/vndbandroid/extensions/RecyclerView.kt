package com.booboot.vndbandroid.extensions

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.util.Pixels

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

fun RecyclerView?.addPaddingBottomIfCanScroll(lifecycleOwner: LifecycleOwner, basePadding: Int, extraPadding: Int) {
    val filtersOnPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        if (this != null) {
            var filtersPadding = basePadding
            if (canScrollVertically(1) || canScrollVertically(-1)) {
                filtersPadding += extraPadding
            }
            setPaddingBottom(filtersPadding)
        }
        true
    }

    this?.viewTreeObserver?.addOnPreDrawListener(filtersOnPreDrawListener)
    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            this@addPaddingBottomIfCanScroll?.viewTreeObserver?.removeOnPreDrawListener(filtersOnPreDrawListener)
        }
    })
}