package com.booboot.vndbandroid.model.vndb

data class Release(
        var id: Int = 0,
        var title: String = "",
        var original: String? = null,
        var released: String? = null,
        var type: String = "",
        var patch: Boolean = false,
        var freeware: Boolean = false,
        var doujin: Boolean = false,
        var languages: List<String> = emptyList(),
        var website: String? = null,
        var notes: String? = null,
        var minage: Int = 0,
        var gtin: String? = null,
        var catalog: String? = null,
        var platforms: List<String> = emptyList(),
        var media: List<Media> = emptyList(),
        val resolution: String? = null,
        var voiced: Int = 0,
        val animation: IntArray = IntArray(2),
        var producers: List<Producer> = emptyList()
)