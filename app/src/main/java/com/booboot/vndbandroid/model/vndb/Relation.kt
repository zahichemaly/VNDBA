package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Relation(var id: Int = 0,
                    var relation: String? = null,
                    var title: String? = null,
                    var original: String? = null) : VNDBCommand() {

    companion object {
        val TYPES = linkedMapOf(
                /* Keep the order here : a VN's relations are sorted in the same order as below */
                "seq" to "Sequel",
                "preq" to "Prequel",
                "ser" to "Same series",
                "set" to "Same setting",
                "side" to "Side story",
                "par" to "Parent story",
                "char" to "Shares characters",
                "alt" to "Alternative version",
                "fan" to "Fandisc",
                "orig" to "Original game"
        )
        var TYPES_KEY = ArrayList(TYPES.keys)
    }
}
