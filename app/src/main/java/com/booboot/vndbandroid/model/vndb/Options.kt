package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.api.SocketPool
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Options(
    var page: Int = 1,
    var results: Int = 25,
    var sort: String? = null,
    var reverse: Boolean = false,
    @Transient var fetchAllPages: Boolean = false,
    @Transient var numberOfPages: Int = 1,
    @Transient var socketIndex: Int = 0,
    @Transient var numberOfSockets: Int = SocketPool.MAX_SOCKETS
)