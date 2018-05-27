package com.booboot.vndbandroid.model.vndb

import java.util.*

data class VNRelation(
        var id: Int = 0,
        var relation: String = "",
        var title: String = "",
        var original: String? = null,
        var official: Boolean = true
) {
    companion object {
        val TYPES = LinkedHashMap<String, String>()
        var TYPES_KEY: List<String>

        init {
            /* Keep the order here : a VN's relations are sorted in the same order as below */
            TYPES.put("seq", "Sequel")
            TYPES.put("preq", "Prequel")
            TYPES.put("ser", "Same series")
            TYPES.put("set", "Same setting")
            TYPES.put("side", "Side story")
            TYPES.put("par", "Parent story")
            TYPES.put("char", "Shares characters")
            TYPES.put("alt", "Alternative version")
            TYPES.put("fan", "Fandisc")
            TYPES.put("orig", "Original game")

            TYPES_KEY = ArrayList(TYPES.keys)
        }
    }
}
