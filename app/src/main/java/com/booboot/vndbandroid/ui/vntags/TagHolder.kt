package com.booboot.vndbandroid.ui.vntags

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import com.google.android.material.button.MaterialButton

class TagHolder(itemView: View, private val callback: TagsAdapter.Callback) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private lateinit var vnTag: VNTag

    init {
        itemView.setOnClickListener(this)
    }

    fun onBind(vnTag: VNTag) = with(itemView) {
        this@TagHolder.vnTag = vnTag

        this as MaterialButton
        val color = ContextCompat.getColor(context, Tag.getScoreColor(vnTag.infos))
        text = vnTag.tag.name
        setTextColor(color)
        rippleColor = ColorStateList.valueOf(color)
        strokeColor = ColorStateList.valueOf(color)
    }

    override fun onClick(v: View?) {
        callback.onChipClicked(vnTag)
    }
}