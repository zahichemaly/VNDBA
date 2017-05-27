package com.booboot.vndbandroid.model.vndbandroid

object Priority {
    const val HIGH = 0
    const val MEDIUM = 1
    const val LOW = 2
    const val BLACKLIST = 3
    const val DEFAULT = "Add to my wishlist"

    fun toString(priority: Int): String = when (priority) {
        HIGH -> "High"
        MEDIUM -> "Medium"
        LOW -> "Low"
        BLACKLIST -> "Blacklist"
        else -> DEFAULT
    }

    fun toShortString(priority: Int): String = when (priority) {
        HIGH -> "H"
        MEDIUM -> "M"
        LOW -> "L"
        BLACKLIST -> "B"
        else -> "-"
    }
}
