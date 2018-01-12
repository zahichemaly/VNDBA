package com.booboot.vndbandroid.model.vndbandroid

data class WishlistItem(
        var vn: Int = 0,
        var added: Int = 0,
        var priority: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as VNlistItem?
        return vn == other.vn
    }

    override fun hashCode(): Int {
        return vn
    }
}