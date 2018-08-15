package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Vnlist(
    override var vn: Long = 0,
    override var added: Int = 0,
    var status: Int = 0,
    var notes: String? = null
) : AccountItem()