package com.booboot.vndbandroid.ui.vntags

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tag_chip.*

class TagHolder(
    override val containerView: View,
    private val onTagClicked: (VNTag) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun onBind(vnTag: VNTag) {
        val color = ContextCompat.getColor(containerView.context, Tag.getScoreColor(vnTag.infos))
        tag.text = vnTag.tag.name
        tag.setTextColor(color)
        tag.rippleColor = ColorStateList.valueOf(color)
        tag.strokeColor = ColorStateList.valueOf(color)
        tag.setOnClickListener { onTagClicked(vnTag) }
    }
}