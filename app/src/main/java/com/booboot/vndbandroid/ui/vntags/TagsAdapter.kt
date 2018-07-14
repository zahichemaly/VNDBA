package com.booboot.vndbandroid.ui.vntags

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndbandroid.VNDetailsTags
import com.booboot.vndbandroid.model.vndbandroid.VNTag

/**
 * Created by od on 22/11/2016.
 */
class TagsAdapter(private val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface Callback {
        fun onTitleClicked(title: String)
        fun onChipClicked(tag: VNTag)
    }

    var items: VNDetailsTags = VNDetailsTags()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is String -> R.layout.collapsing_tab
        else -> R.layout.tag_chip
    }

    private fun getItem(position: Int): Any? {
        var i = 0
        items.all.forEach {
            if (i++ == position) return it.key
            it.value.forEach {
                if (i++ == position) return it
            }
        }
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.collapsing_tab -> TagTabHolder(v, callback)
            else -> TagHolder(v, callback)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is TagTabHolder -> holder.onBind(item as String)
            is TagHolder -> holder.onBind(item as VNTag)
        }
    }

    override fun getItemCount() = items.all.keys.size + items.all.values.sumBy { it.size }
}