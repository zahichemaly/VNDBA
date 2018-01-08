package com.booboot.vndbandroid.model.vndb

data class Response<T>(
        var error: Error? = null,
        var results: T? = null,
        var ok: Boolean = false
)