package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.model.vndb.Label.Companion.STATUSES
import com.booboot.vndbandroid.model.vndb.Label.Companion.WISHLISTS
import com.booboot.vndbandroid.ui.vnlist.VNLabelItem
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
    @Transient var labelItems = listOf<VNLabelItem>()

    fun labelIds() = labels.map { it.id }.toSet()

    fun labels(group: Set<Long>) = labels.filter { it.id in group }

    fun firstStatus() = labels.find { it.id in STATUSES }

    fun firstWishlist() = labels.find { it.id in WISHLISTS }

    fun setLabelItems() {
        labelItems = labels.filterNot { it.id == Label.VOTED.id }.take(7).map { VNLabelItem(it) }
    }
}