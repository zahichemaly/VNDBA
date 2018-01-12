package com.booboot.vndbandroid.model.vndbandroid

import java.util.ArrayList
import java.util.LinkedHashMap

object Category {
    val CATEGORIES = LinkedHashMap<String, String>()
    var CATEGORIES_KEYS: List<String>

    init {
        CATEGORIES.put("tech", "Technical")
        CATEGORIES.put("cont", "Content")
        CATEGORIES.put("ero", "Sexual content")

        CATEGORIES.put("director", "Director")
        CATEGORIES.put("songs", "Vocals")
        CATEGORIES.put("music", "Composer")
        CATEGORIES.put("scenario", "Scenario")
        CATEGORIES.put("art", "Artist")
        CATEGORIES.put("chardesign", "Character design")
        CATEGORIES.put("staff", "Staff")

        CATEGORIES_KEYS = ArrayList(CATEGORIES.keys)
    }
}
