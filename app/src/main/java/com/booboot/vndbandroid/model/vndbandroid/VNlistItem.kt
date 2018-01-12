package com.booboot.vndbandroid.model.vndbandroid

data class VNlistItem(
        var status: Int = 0,
        var notes: String
) : CacheItem()