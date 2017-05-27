package com.booboot.vndbandroid.model.vndbandroid

object Category {
    val CATEGORIES = linkedMapOf(
            "tech" to "Technical",
            "cont" to "Content",
            "ero" to "Sexual content",

            "director" to "Director",
            "songs" to "Vocals",
            "music" to "Composer",
            "scenario" to "Scenario",
            "art" to "Artist",
            "chardesign" to "Character design",
            "staff" to "Staff")

    var CATEGORIES_KEYS: List<String> = ArrayList(CATEGORIES.keys)
}
