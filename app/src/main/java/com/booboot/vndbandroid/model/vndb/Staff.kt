package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Staff(
    var id: Int = 0,
    var name: String = "",
    var original: String? = null,
    var _gender: String? = null,
    var language: String = "",
    var links: Links = Links(),
    var description: String? = null,
    var aliases: List<Array<Any>> = emptyList(),
    var main_alias: Int = 0,
    var vns: List<StaffVns> = emptyList(),
    var voiced: List<StaffVoiced> = emptyList(),
    var note: String? = null
) {
    var gender = _gender
        set(gender) {
            field = gender?.toUpperCase() ?: gender
        }
}