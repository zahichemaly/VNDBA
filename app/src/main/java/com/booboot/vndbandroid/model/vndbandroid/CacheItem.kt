package com.booboot.vndbandroid.model.vndbandroid

abstract class CacheItem {
    var vn: Int = 0
    var added: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val cacheItem = other as CacheItem?
        return vn == cacheItem!!.vn
    }

    override fun hashCode(): Int {
        return vn
    }
}