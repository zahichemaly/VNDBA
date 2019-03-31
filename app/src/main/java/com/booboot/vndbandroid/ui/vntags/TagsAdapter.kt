package com.booboot.vndbandroid.ui.vntags

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.diff.VNDetailsTagsDiffCallback
import com.booboot.vndbandroid.model.vndbandroid.VNDetailsTags
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import com.booboot.vndbandroid.ui.base.BaseAdapter

/**
 * Created by od on 22/11/2016.
 */
class TagsAdapter(private val callback: Callback) : BaseAdapter<RecyclerView.ViewHolder>() {
    interface Callback {
        fun onTitleClicked(title: String)
        fun onChipClicked(tag: VNTag)
    }

    var items: VNDetailsTags = VNDetailsTags()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(VNDetailsTagsDiffCallback(field, value))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemViewType(position: Int): Int = when (items.get(position)) {
        is String -> R.layout.collapsing_tab
        else -> R.layout.tag_chip
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.collapsing_tab -> TagTabHolder(v, callback)
            else -> TagHolder(v, callback)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.get(position)
        when (holder) {
            is TagTabHolder -> holder.onBind(item as String, items.all[item]?.isEmpty() != false)
            is TagHolder -> holder.onBind(item as VNTag)
        }
    }

    override fun getItemCount() = items.count()
}