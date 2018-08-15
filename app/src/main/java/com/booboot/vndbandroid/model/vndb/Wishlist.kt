package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Wishlist(
    override var vn: Long = 0,
    override var added: Int = 0,
    var priority: Int = 0
) : AccountItem()