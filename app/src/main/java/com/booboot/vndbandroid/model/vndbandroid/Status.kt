package com.booboot.vndbandroid.model.vndbandroid

object Status {
    const val UNKNOWN = 0
    const val PLAYING = 1
    const val FINISHED = 2
    const val STALLED = 3
    const val DROPPED = 4
    const val DEFAULT = "Add to my VN list"

    fun toString(status: Int): String = when (status) {
        UNKNOWN -> "Unknown"
        PLAYING -> "Playing"
        FINISHED -> "Finished"
        STALLED -> "Stalled"
        DROPPED -> "Dropped"
        else -> DEFAULT
    }

    fun toShortString(status: Int): String = when (status) {
        UNKNOWN -> "?"
        PLAYING -> "P"
        FINISHED -> "F"
        STALLED -> "S"
        DROPPED -> "D"
        else -> "-"
    }
}
