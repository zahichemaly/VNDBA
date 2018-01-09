package com.booboot.vndbandroid.model.vndb

data class Options(
        var page: Int = 1,
        var results: Int = 25,
        var sort: String? = null,
        var reverse: Boolean = false,
        @Transient var fetchAllPages: Boolean = false,
        @Transient var numberOfPages: Int = 1
)