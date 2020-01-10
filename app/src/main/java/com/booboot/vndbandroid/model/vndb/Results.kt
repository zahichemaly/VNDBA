package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Results<T>(
    var num: Int = 0,
    var more: Boolean = false,
    var items: MutableList<T> = mutableListOf()
)