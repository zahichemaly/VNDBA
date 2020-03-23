package com.booboot.vndbandroid.ui.filters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.selectIf
import com.booboot.vndbandroid.model.vndbandroid.SortItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.filter_button.*

class SortItemHolder(override val containerView: View, private val onSortClicked: (SortItem) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(item: SortItem) {
        button.setText(item.title)
        button.selectIf(item.selected, R.color.textColorPrimaryReverse)
        button.rippleColor = button.strokeColor
        button.setOnClickListener { onSortClicked(item) }
    }
}