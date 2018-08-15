package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Votelist(
    override var vn: Long = 0,
    override var added: Int = 0,
    var vote: Int = 0
) : AccountItem()