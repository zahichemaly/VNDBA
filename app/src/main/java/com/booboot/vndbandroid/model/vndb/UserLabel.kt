package com.booboot.vndbandroid.model.vndb

import androidx.annotation.ColorRes
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Label.Companion.BLACKLIST
import com.booboot.vndbandroid.model.vndb.Label.Companion.DROPPED
import com.booboot.vndbandroid.model.vndb.Label.Companion.FINISHED
import com.booboot.vndbandroid.model.vndb.Label.Companion.PLAYING
import com.booboot.vndbandroid.model.vndb.Label.Companion.STALLED
import com.booboot.vndbandroid.model.vndb.Label.Companion.UNKNOWN
import com.booboot.vndbandroid.model.vndb.Label.Companion.WISHLIST
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLabel(
    var id: Long = 0,
    var label: String,
    var private: Boolean = true
) {
    @ColorRes
    fun color() = when (id) {
        PLAYING.id -> R.color.playing
        FINISHED.id -> R.color.finished
        STALLED.id -> R.color.stalled
        DROPPED.id -> R.color.dropped
        UNKNOWN.id -> R.color.unknown
        WISHLIST.id -> R.color.playing
        BLACKLIST.id -> R.color.unknown
        else -> R.color.textColorPrimary
    }
}