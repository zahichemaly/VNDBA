package com.booboot.vndbandroid.ui.filters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.model.vndbandroid.FilterTitle
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.filter_title.*

class FilterTitleHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(item: FilterTitle) {
        text.setText(item.title)
        icon.setImageResource(item.icon)
        containerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin = item.marginBottom
        }
    }
}