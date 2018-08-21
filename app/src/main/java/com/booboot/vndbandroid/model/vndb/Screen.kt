package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Screen(
    var image: String = "",
    var rid: Int = 0,
    var nsfw: Boolean = false,
    var height: Int = 0,
    var width: Int = 0
)