package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.Wishlist
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class WishlistDao(
    @Id(assignable = true) var vn: Long = 0,
    var added: Int = 0,
    var priority: Int = 0
) {
    constructor(wishlist: Wishlist) : this(wishlist.vn, wishlist.added, wishlist.priority)

    fun toBo() = Wishlist(vn, added, priority)
}