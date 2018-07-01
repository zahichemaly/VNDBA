package com.booboot.vndbandroid.util

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue

class GridAutofitLayoutManager : GridLayoutManager {
    private var mColumnWidth: Int = 0
        set(value) {
            if (value > 0 && value != field) {
                field = value
                mColumnWidthChanged = true
            }
        }
    private var mColumnWidthChanged = true

    /* Initially set spanCount to 1, will be changed automatically later. */
    constructor(context: Context, columnWidth: Int) : super(context, 1) {
        mColumnWidth = checkedColumnWidth(context, columnWidth)
    }

    constructor(context: Context, columnWidth: Int, orientation: Int, reverseLayout: Boolean) : super(context, 1, orientation, reverseLayout) {
        mColumnWidth = checkedColumnWidth(context, columnWidth)
    }

    /**
     *  Set default columnWidth value (48dp here). It is better to move this constant
     *  to static constant on top, but we need context to convert it to dp, so can't really
     *  do so.
     */
    private fun checkedColumnWidth(context: Context, columnWidth: Int): Int =
            if (columnWidth <= 0) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, context.resources.displayMetrics).toInt()
            else columnWidth

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        val width = width
        val height = height
        if (mColumnWidthChanged && mColumnWidth > 0 && width > 0 && height > 0) {
            val totalSpace = if (orientation == LinearLayoutManager.VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = Math.max(1, totalSpace / mColumnWidth)
            setSpanCount(spanCount)
            mColumnWidthChanged = false
        }
        super.onLayoutChildren(recycler, state)
    }
}
