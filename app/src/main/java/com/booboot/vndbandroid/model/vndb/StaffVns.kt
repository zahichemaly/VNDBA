package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffVns(
    var id: Int = 0,
    var aid: Int = 0,
    var role: String = "",
    var note: String? = null
)