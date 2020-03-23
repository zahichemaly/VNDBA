package com.booboot.vndbandroid.model.vndbandroid

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

abstract class FilterItem(
    open val id: Long
)

data class FilterTitle(
    override val id: Long,
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val marginBottom: Int
) : FilterItem(id)

data class SortItem(
    override val id: Long,
    @StringRes val title: Int,
    val selected: Boolean
) : FilterItem(id)

data class FilterCheckbox(
    override val id: Long,
    @StringRes val title: Int,
    val selected: Boolean
) : FilterItem(id) {
    var onClicked: (FilterCheckbox) -> Unit = {}
}