package com.booboot.vndbandroid.model.vndbandroid

object Priority {
    val HIGH = 0
    val MEDIUM = 1
    val LOW = 2
    val BLACKLIST = 3
    val DEFAULT = "Add to my wishlist"

    fun toString(priority: Int) = when (priority) {
        0 -> "High"
        1 -> "Medium"
        2 -> "Low"
        3 -> "Blacklist"
        else -> DEFAULT
    }

    fun toShortString(priority: Int) = when (priority) {
        0 -> "H"
        1 -> "M"
        2 -> "L"
        3 -> "B"
        else -> "-"
    }
}
