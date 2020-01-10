package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.model.vndb.Label.Companion.STATUSES
import com.booboot.vndbandroid.model.vndb.Label.Companion.WISHLISTS
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserList(
    var vn: Long = 0,
    var added: Long = 0,
    var lastmod: Long = 0,
    var voted: Long? = null,
    var vote: Int? = null,
    var notes: String? = null,
    var started: String? = null,
    var finished: String? = null,
    var labels: Set<Label> = hashSetOf()
) {
    fun labelIds() = labels.map { it.id }.toSet()

    fun labels(group: Set<Long>) = labels.filter { it.id in group }

    fun firstStatus() = labels(STATUSES).getOrNull(0)

    fun firstWishlist() = labels(WISHLISTS).getOrNull(0)
}