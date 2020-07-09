package com.booboot.vndbandroid.ui.filters

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.selectIf
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.filter_button.*
import kotlinx.android.synthetic.main.filter_checkbox.*
import kotlinx.android.synthetic.main.filter_title.*

data class FilterTitleItem(
    val _id: Long,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) : Item(), ExpandableItem {
    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            titleView.setText(title)
            iconView.setImageResource(icon)
            update()

            containerView.setOnClickListener {
                expandableGroup.onToggleExpanded()
                update()
            }
        }
    }

    private fun GroupieViewHolder.update() {
        arrow.setImageResource(if (expandableGroup.isExpanded) R.drawable.ic_keyboard_arrow_up_24dp else R.drawable.ic_keyboard_arrow_down_24dp)
    }

    override fun getLayout() = R.layout.filter_title

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    override fun getId() = _id
}

data class SortItem(
    private val _id: Long,
    @StringRes val title: Int,
    val selected: Boolean
) : Item() {
    var onSortClicked: (SortItem) -> Unit = {}

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            filterButton.setText(title)
            filterButton.selectIf(selected, R.color.textColorPrimaryReverse)
            filterButton.rippleColor = filterButton.strokeColor
            filterButton.setOnClickListener { onSortClicked(this@SortItem) }
        }
    }

    override fun getId() = _id
    override fun getLayout() = R.layout.filter_button
}

data class FilterCheckboxItem(
    private val _id: Long,
    @StringRes val title: Int,
    val selected: Boolean
) : Item() {
    var onClicked: (FilterCheckboxItem) -> Unit = {}

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            filterCheckbox.setText(title)
            filterCheckbox.isChecked = selected
            filterCheckbox.setOnClickListener { onClicked(this@FilterCheckboxItem) }
        }
    }

    override fun getId() = _id
    override fun getLayout() = R.layout.filter_checkbox
}