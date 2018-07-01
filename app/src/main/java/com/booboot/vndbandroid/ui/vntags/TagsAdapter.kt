package com.booboot.vndbandroid.ui.vntags

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.diff.TagsDiffCallback
import com.booboot.vndbandroid.model.vndbandroid.VNTag

/**
 * Created by od on 22/11/2016.
 */
class TagsAdapter(private val callback: (VNTag) -> Unit) : RecyclerView.Adapter<TagHolder>() {
    var items: List<VNTag> = emptyList()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(TagsDiffCallback(field, value))
            field = value
            diffResult.dispatchUpdatesTo(this@TagsAdapter)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.tag_chip, parent, false)
        return TagHolder(v, callback)
    }

    override fun onBindViewHolder(holder: TagHolder, position: Int) = holder.onBind(items[position])

    override fun getItemCount() = items.size
}