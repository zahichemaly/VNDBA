package com.booboot.vndbandroid.diff

import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.model.vndbandroid.FilterItem

class FilterDiffCallback(private var oldItems: List<FilterItem>, private var newItems: List<FilterItem>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition] == newItems[newItemPosition]
}