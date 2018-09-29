package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R

object Priority {
    const val HIGH = 0
    const val MEDIUM = 1
    const val LOW = 2
    const val BLACKLIST = 3
    const val DEFAULT = "Add to my wishlist"
    val ALL = listOf(HIGH, MEDIUM, LOW, BLACKLIST)

    fun toString(priority: Int?) = when (priority) {
        0 -> "High"
        1 -> "Medium"
        2 -> "Low"
        3 -> "Blacklist"
        else -> null
    }

    fun toShortString(priority: Int?): String = when (priority) {
        0 -> "H"
        1 -> "M"
        2 -> "L"
        3 -> "B"
        else -> App.context.getString(R.string.dash)
    }
}