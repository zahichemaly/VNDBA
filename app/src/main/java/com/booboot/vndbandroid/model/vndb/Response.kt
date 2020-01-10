package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response<T>(
    var error: Error? = null,
    var results: T? = null,
    var ok: Boolean = false
)