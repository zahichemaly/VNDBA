package com.booboot.vndbandroid.ui.vntags

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import kotlinx.android.synthetic.main.tag_chip.view.*
import kotlinx.android.synthetic.main.vn_card.view.*

class TagHolder(itemView: View, private val callback: (VNTag) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private lateinit var vnTag: VNTag

    init {
        itemView.chip.setOnClickListener(this)
    }

    fun onBind(vnTag: VNTag) = with(itemView) {
        this@TagHolder.vnTag = vnTag

        chip.chipText = vnTag.tag.name
        chip.chipIcon = ContextCompat.getDrawable(context, Tag.getScoreImage(vnTag.infos))
    }

    override fun onClick(v: View?) {
        callback(vnTag)
    }
}