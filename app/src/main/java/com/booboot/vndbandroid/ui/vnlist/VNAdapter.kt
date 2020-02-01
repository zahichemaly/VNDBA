package com.booboot.vndbandroid.ui.vnlist

import android.view.View
import android.view.ViewGroup
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.inflate
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.base.BaseAdapter

class VNAdapter(
    private val onVnClicked: (View, VN) -> Unit,
    private val showFullDate: Boolean = false,
    private val showRank: Boolean = false,
    private val showRating: Boolean = false,
    private val showPopularity: Boolean = false,
    private val showVoteCount: Boolean = false
) : BaseAdapter<VNHolder>() {
    var data = VnlistData()
        set(value) {
            field = value
            onUpdateInternal()
            value.diffResult?.dispatchUpdatesTo(this) ?: notifyChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VNHolder(parent.inflate(R.layout.vn_card), onVnClicked)

    override fun onBindViewHolder(holder: VNHolder, position: Int) {
        val vn = data.items.vns.values.toList()[position]
        holder.bind(
            vn,
            data.items.userList[vn.id],
            showFullDate,
            showRank,
            showRating,
            showPopularity,
            showVoteCount
        )
    }

    override fun getItemCount() = data.items.vns.values.size

    override fun getItemId(position: Int) = data.items.vns.values.toList()[position].id
}