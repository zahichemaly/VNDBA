package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R

object Status {
    const val UNKNOWN = 0
    const val PLAYING = 1
    const val FINISHED = 2
    const val STALLED = 3
    const val DROPPED = 4
    const val DEFAULT = "Add to my VN list"
    val ALL = listOf(PLAYING, FINISHED, STALLED, DROPPED, UNKNOWN)

    fun toString(status: Int?) = when (status) {
        0 -> "Unknown"
        1 -> "Playing"
        2 -> "Finished"
        3 -> "Stalled"
        4 -> "Dropped"
        else -> null
    }

    fun toShortString(status: Int?): String = when (status) {
        0 -> "?"
        1 -> "P"
        2 -> "F"
        3 -> "S"
        4 -> "D"
        else -> App.context.getString(R.string.dash)
    }
}
