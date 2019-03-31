package com.booboot.vndbandroid.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {
    var onFinishDrawing: () -> Unit = {}

    override fun onViewAttachedToWindow(holder: T) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.post {
            onFinishDrawing()
        }
    }
}