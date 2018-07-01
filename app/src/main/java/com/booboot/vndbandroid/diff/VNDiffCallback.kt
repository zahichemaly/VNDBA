package com.booboot.vndbandroid.diff

import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.model.vndb.VN

class VNDiffCallback(private var oldItems: List<VN>, private var newItems: List<VN>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldItems[oldItemPosition] == newItems[newItemPosition]
}