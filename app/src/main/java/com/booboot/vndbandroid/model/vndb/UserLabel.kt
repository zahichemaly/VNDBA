package com.booboot.vndbandroid.model.vndb

import androidx.annotation.ColorRes
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLabel(
    var id: Long = 0,
    var label: String,
    var private: Boolean = true
) {
    @ColorRes
    fun color() = Label(id, label).buttonColor()
}