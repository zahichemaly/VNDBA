package com.booboot.vndbandroid.ui.filters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.model.vndbandroid.FilterCheckbox
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.filter_checkbox.*

class FilterCheckboxHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(item: FilterCheckbox) {
        button.setText(item.title)
        button.isChecked = item.selected
        button.setOnClickListener { item.onClicked(item) }
    }
}