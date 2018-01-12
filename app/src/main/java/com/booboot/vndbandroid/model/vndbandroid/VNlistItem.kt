package com.booboot.vndbandroid.model.vndbandroid

data class VNlistItem(
        var vn: Int = 0,
        var added: Int = 0,
        var status: Int = 0,
        var notes: String
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