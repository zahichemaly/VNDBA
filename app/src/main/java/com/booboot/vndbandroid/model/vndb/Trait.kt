package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Trait(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var meta: Boolean = false,
    var chars: Int = 0,
    var aliases: List<String> = emptyList(),
    var parents: List<Long> = emptyList()
) {
    override fun toString(): String {
        return "Trait(name=$name)"
    }
}