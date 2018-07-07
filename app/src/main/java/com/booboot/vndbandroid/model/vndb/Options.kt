package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.api.SocketPool
import com.fasterxml.jackson.annotation.JsonIgnore

data class Options(
    var page: Int = 1,
    var results: Int = 25,
    var sort: String? = null,
    var reverse: Boolean = false,
    @field:JsonIgnore var fetchAllPages: Boolean = false,
    @field:JsonIgnore var numberOfPages: Int = 1,
    @field:JsonIgnore var socketIndex: Int = 0,
    @field:JsonIgnore var numberOfSockets: Int = SocketPool.MAX_SOCKETS
)