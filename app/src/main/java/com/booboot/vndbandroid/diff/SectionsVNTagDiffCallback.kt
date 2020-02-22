package com.booboot.vndbandroid.diff

import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.model.vndbandroid.Sections
import com.booboot.vndbandroid.model.vndbandroid.VNTag

class SectionsVNTagDiffCallback(private var oldItems: Sections<VNTag>, private var newItems: Sections<VNTag>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.count()

    override fun getNewListSize() = newItems.count()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.get(oldItemPosition)
        val newItem = newItems.get(newItemPosition)
        return when {
            oldItem is VNTag && newItem is VNTag -> oldItem.tag.id == newItem.tag.id
            oldItem is String && newItem is String -> oldItem == newItem
            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.get(oldItemPosition)
        val newItem = newItems.get(newItemPosition)
        return when {
            oldItem is VNTag && newItem is VNTag -> oldItem.tag == newItem.tag
            oldItem is String && newItem is String -> oldItem == newItem
            else -> false
        }
    }
}