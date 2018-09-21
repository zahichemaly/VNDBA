package com.booboot.vndbandroid.factory

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.ui.home.HomeActivity
import com.booboot.vndbandroid.ui.vnlist.VNAdapter
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels

// TODO remove
object VNCardFactory {
    fun setupList(context: Context?, vnList: RecyclerView, adapter: VNAdapter) {
        if (context != null) {
            vnList.layoutManager = GridAutofitLayoutManager(context, Pixels.px(300))
            vnList.adapter = adapter
            if (context is HomeActivity) {
                // TODO there is likely a Behavior to attach to the FAB so this may be useless!
                /* Automatically hide the FAB button when reaching the bottom of the RecyclerView (only in HomeActivity of course) */
                vnList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            val visibleItemCount = recyclerView.layoutManager?.childCount ?: 0
                            val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                            val pastVisiblesItems = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                            context.toggleFloatingSearchButton(visibleItemCount + pastVisiblesItems < totalItemCount)
                        } else {
                            context.toggleFloatingSearchButton(true)
                        }
                    }
                })
            }
        }
    }
}