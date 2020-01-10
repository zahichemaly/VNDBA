package com.booboot.vndbandroid.diff

import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.model.vndbandroid.AccountItems

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
        return oldVn.equalsBasic(newVn)
            && oldItems.userList[oldVn.id] == newItems.userList[newVn.id]
    }
}