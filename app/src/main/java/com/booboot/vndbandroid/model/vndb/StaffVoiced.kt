package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffVoiced(
    var id: Int = 0,
    var aid: Int = 0,
    var cid: Int = 0,
    var note: String? = null
)