package com.booboot.vndbandroid.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.ui.base.BaseAdapter
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun RecyclerView.hideOnBottom(fab: FloatingActionButton?) {
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

fun RecyclerView.saveState() = layoutManager?.onSaveInstanceState()

fun RecyclerView.restoreState(fragment: BaseFragment<*>) = (adapter as? BaseAdapter)?.onFinishDrawing?.add {
    (fragment.viewModel.layoutState ?: fragment.layoutState)?.let {
        layoutManager?.onRestoreInstanceState(it)
        fragment.viewModel.layoutState = null
        fragment.layoutState = null
    }
}