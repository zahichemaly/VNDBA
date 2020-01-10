package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterVoiced(
    var id: Int = 0,
    var aid: Int = 0,
    var vid: Int = 0,
    var note: String? = null
)