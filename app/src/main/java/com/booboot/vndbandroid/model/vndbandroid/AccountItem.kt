package com.booboot.vndbandroid.model.vndbandroid

abstract class AccountItem constructor(
        var vn: Int = 0,
        var added: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AccountItem?
        return vn == other.vn
    }

    override fun hashCode(): Int {
        return vn
    }
}