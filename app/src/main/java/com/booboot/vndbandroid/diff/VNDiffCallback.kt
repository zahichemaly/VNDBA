package com.booboot.vndbandroid.diff

import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.model.vndb.AccountItems

class VNDiffCallback(private var oldItems: AccountItems, private var newItems: AccountItems) : DiffUtil.Callback() {
    private val oldVns = oldItems.vns.values.toList()
    private val newVns = newItems.vns.values.toList()

    override fun getOldListSize() = oldVns.size

    override fun getNewListSize() = newVns.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldVns[oldItemPosition].id == newVns[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldVn = oldVns[oldItemPosition]
        val newVn = newVns[newItemPosition]
        return oldVn == newVn
            && oldItems.vnlist[oldVn.id] == newItems.vnlist[newVn.id]
            && oldItems.votelist[oldVn.id] == newItems.votelist[newVn.id]
            && oldItems.wishlist[oldVn.id] == newItems.wishlist[newVn.id]
    }
}