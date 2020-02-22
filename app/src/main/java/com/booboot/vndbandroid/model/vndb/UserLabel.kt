package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLabel(
    var id: Long = 0,
    var label: String,
    var private: Boolean
)