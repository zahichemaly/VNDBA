package com.booboot.vndbandroid.model.vndb

data class Options(
        var page: Int = 1,
        var results: Int = 25,
        var sort: String? = null,
        var reverse: Boolean = false,
        var fetchAllPages: Boolean = false,
        var numberOfPages: Int = 0
)