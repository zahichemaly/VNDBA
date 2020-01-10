package com.booboot.vndbandroid.diff

import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.vnrelations.RelationsData

class RelationsDiffCallback(private var oldItems: RelationsData, private var newItems: RelationsData) : DiffUtil.Callback() {
    private val oldVn = oldItems.items.vns[oldItems.vn.id]
    val newVn = newItems.items.vns[newItems.vn.id]

    override fun getOldListSize() = oldVn?.let {
        it.relations.size + it.anime.size
    } ?: 0

    override fun getNewListSize() = newVn?.let {
        it.relations.size + it.anime.size
    } ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldType = getItemViewType(oldItemPosition, oldVn)
        val newType = getItemViewType(oldItemPosition, newVn)
        if (oldType != newType) return false
        return when (oldType) {
            R.layout.anime_card -> oldVn?.anime?.get(oldItemPosition)?.id == newVn?.anime?.get(newItemPosition)?.id
            else -> oldVn?.relations?.get(oldItemPosition - oldVn.anime.size)?.id == newVn?.relations?.get(newItemPosition - newVn.anime.size)?.id
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldType = getItemViewType(oldItemPosition, oldVn)
        val newType = getItemViewType(oldItemPosition, newVn)
        if (oldType != newType) return false
        return when (oldType) {
            R.layout.anime_card -> oldVn?.anime?.get(oldItemPosition) == newVn?.anime?.get(newItemPosition) && oldVn == newVn
            else -> {
                val oldRelation = oldVn?.relations?.get(oldItemPosition - oldVn.anime.size)
                val newRelation = newVn?.relations?.get(newItemPosition - newVn.anime.size)
                if (oldRelation != newRelation) return false
                val oldRelationVn = oldItems.relations[oldRelation?.id ?: 0]
                val newRelationVn = newItems.relations[newRelation?.id ?: 0]
                if (oldRelationVn?.equalsBasic(newRelationVn) != true) return false
                val oldUserList = oldItems.items.userList[oldRelation?.id ?: 0]
                val newUserList = newItems.items.userList[newRelation?.id ?: 0]
                if (oldUserList != newUserList) return false
                true
            }
        }
    }

    companion object {
        fun getItemViewType(position: Int, vn: VN?) = if (position < vn?.anime?.size ?: 0) {
            R.layout.anime_card
        } else {
            R.layout.vn_card
        }
    }
}