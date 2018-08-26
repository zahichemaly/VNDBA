package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Relation(
    var id: Long = 0,
    var relation: String = "",
    var title: String = "",
    var original: String? = null,
    var official: Boolean = true
)

val RELATIONS = linkedMapOf(
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