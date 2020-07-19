package com.booboot.vndbandroid.model.vndbandroid

import java.util.*

object Category {
    val CATEGORIES = LinkedHashMap<String, String>()
    var CATEGORIES_KEYS: List<String>

    init {
        CATEGORIES["tech"] = "Technical"
        CATEGORIES["cont"] = "Content"
        CATEGORIES["ero"] = "Sexual content"

        CATEGORIES["director"] = "Director"
        CATEGORIES["songs"] = "Vocals"
        CATEGORIES["music"] = "Composer"
        CATEGORIES["scenario"] = "Scenario"
        CATEGORIES["art"] = "Artist"
        CATEGORIES["chardesign"] = "Character design"
        CATEGORIES["staff"] = "Staff"

        CATEGORIES_KEYS = ArrayList(CATEGORIES.keys)
    }
}