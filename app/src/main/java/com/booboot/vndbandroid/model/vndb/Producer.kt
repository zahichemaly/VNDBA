package com.booboot.vndbandroid.model.vndb

data class Producer(
        var id: Int = 0,
        var developer: Boolean = false,
        var publisher: Boolean = false,
        var name: String = "",
        var language: String = "",
        var original: String? = null,
        var description: String? = null,
        var type: String = ""
)