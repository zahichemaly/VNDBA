package com.booboot.vndbandroid.ui.base

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {
    var onFinishDrawing = mutableListOf<() -> Unit>()
    var onUpdate: (Boolean) -> Unit = {}

    protected fun onUpdateInternal() = Handler(Looper.getMainLooper()).post {
        onUpdate(itemCount <= 0)
        /* If the Adapter is empty: onViewAttachedToWindow is not called, so we're calling onFinishDrawingInternal() here */
        if (itemCount <= 0) {
            onFinishDrawingInternal()
        }
    }

    private fun onFinishDrawingInternal() {
        onFinishDrawing.forEach { it() }
        onFinishDrawing.clear()
    }

    protected fun notifyChanged() {
        super.notifyDataSetChanged()
        onUpdateInternal()
    }

    /**
     * Latest existing callback to get notified that the Adapter has done drawing its views after an update.
     */
    override fun onViewAttachedToWindow(holder: T) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.post {
            onFinishDrawingInternal()
        }
    }
}