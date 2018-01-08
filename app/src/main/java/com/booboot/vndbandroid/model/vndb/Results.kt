package com.booboot.vndbandroid.model.vndb

data class Results<T>(
        var num: Int = 0,
        var more: Boolean = false,
        var items: MutableList<T> = mutableListOf()
)