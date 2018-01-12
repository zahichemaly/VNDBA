package com.booboot.vndbandroid.model.vndbandroid

object Status {
    val UNKNOWN = 0
    val PLAYING = 1
    val FINISHED = 2
    val STALLED = 3
    val DROPPED = 4
    val DEFAULT = "Add to my VN list"

    fun toString(status: Int) = when (status) {
        0 -> "Unknown"
        1 -> "Playing"
        2 -> "Finished"
        3 -> "Stalled"
        4 -> "Dropped"
        else -> DEFAULT
    }

    fun toShortString(status: Int) = when (status) {
        0 -> "?"
        1 -> "P"
        2 -> "F"
        3 -> "S"
        4 -> "D"
        else -> "-"
    }
}
