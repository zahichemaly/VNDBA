package com.booboot.vndbandroid.model.vndbandroid

import androidx.recyclerview.widget.DiffUtil

data class FilterData(
    val items: List<FilterItem> = listOf(),
    var diffResult: DiffUtil.DiffResult? = null
)