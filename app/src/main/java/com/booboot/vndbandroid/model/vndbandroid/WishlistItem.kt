package com.booboot.vndbandroid.model.vndbandroid

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class WishlistItem : CacheItem {
    var priority: Int = 0

    constructor()

    constructor(vn: Int, added: Int, priority: Int) : super(vn, added) {
        this.priority = priority
    }
}