package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Fields(
    var vote: Int = 0,
    var status: Int = 0,
    var notes: String? = null,
    var priority: Int = 0
)