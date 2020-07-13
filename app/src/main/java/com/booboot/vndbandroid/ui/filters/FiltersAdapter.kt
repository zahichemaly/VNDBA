package com.booboot.vndbandroid.ui.filters

import android.content.res.ColorStateList
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.selectIf
import com.booboot.vndbandroid.model.vndb.UserLabel
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.filter_checkbox.*
import kotlinx.android.synthetic.main.filter_subtitle.*
import kotlinx.android.synthetic.main.filter_title.*
import kotlinx.android.synthetic.main.label_item.*
import kotlinx.android.synthetic.main.sort_item.*
import kotlinx.android.synthetic.main.vote_button.*

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

data class SortItem(
    private val id: Long,
    @StringRes val title: Int,
    val selected: Boolean
) : Item() {
    var onSortClicked: (SortItem) -> Unit = {}

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            sortButton.setText(title)
            sortButton.selectIf(selected, R.color.textColorPrimaryReverse)
            sortButton.rippleColor = sortButton.strokeColor
            sortButton.setOnClickListener { onSortClicked(this@SortItem) }
        }
    }

    override fun getId() = id
    override fun getLayout() = R.layout.sort_item
}

data class FilterSubtitleItem(
    @DrawableRes private val icon: Int,
    @StringRes private val title: Int
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            subtitleIcon.setImageResource(icon)
            subtitleText.setText(title)
        }
    }

    override fun getId() = title.hashCode().toLong()
    override fun getLayout() = R.layout.filter_subtitle
}

data class LabelItem(
    var label: UserLabel,
    val selected: Boolean
) : Item() {
    var onLabelClicked: (UserLabel) -> Unit = {}

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            labelButton.text = label.label
            val color = ContextCompat.getColor(containerView.context, label.color())
            labelButton.setTextColor(color)
            labelButton.strokeColor = ColorStateList.valueOf(color)
            labelButton.selectIf(selected, if (label.color() == R.color.textColorPrimary) R.color.textColorPrimaryReverse else R.color.white)
            labelButton.setOnClickListener { onLabelClicked(label) }
        }
    }

    override fun getId() = label.id
    override fun getLayout() = R.layout.label_item
}

data class VoteItem(
    var label: UserLabel,
    val selected: Boolean
) : Item() {
    var onLabelClicked: (UserLabel) -> Unit = {}

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            voteButton.text = label.label
            voteButton.selectIf(selected)
            voteButton.setOnClickListener { onLabelClicked(label) }
        }
    }

    override fun getId() = label.id
    override fun getLayout() = R.layout.vote_button
}