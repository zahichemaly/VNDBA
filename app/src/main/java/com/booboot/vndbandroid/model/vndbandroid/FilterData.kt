package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.ui.filters.FilterSubtitleItem
import com.xwray.groupie.kotlinandroidextensions.Item

data class FilterData(
    @SortOptions val sort: Long,
    val reverseSort: Boolean,
    val categorizedLabels: MutableMap<FilterSubtitleItem, MutableList<Item>>,
    val selectedFilters: Set<Long>
)