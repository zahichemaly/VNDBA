package com.booboot.vndbandroid.model.vndbandroid

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class CacheItem {
    var vn: Int = 0
    var added: Int = 0

    constructor()

    constructor(vn: Int, added: Int) {
        this.vn = vn
        this.added = added
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is CacheItem) return false
        return vn == other.vn
    }

    override fun hashCode(): Int {
        return vn
    }
}
