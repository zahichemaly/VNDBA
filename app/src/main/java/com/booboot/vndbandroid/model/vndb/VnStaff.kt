package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.R
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VnStaff(
    var sid: Int = 0,
    var aid: Int = 0,
    var name: String = "",
    var original: String? = null,
    var role: String = "",
    var note: String? = null
) {
    fun icon(): Int = when (role) {
        "songs" -> R.drawable.ic_mic_white_48dp
        "staff" -> R.drawable.ic_assignment_ind_white_48dp
        "scenario" -> R.drawable.ic_text_format_white_48dp
        "music" -> R.drawable.ic_audiotrack_white_48dp
        "art" -> R.drawable.ic_brush_white_48dp
        "director" -> R.drawable.ic_theaters_white_48dp
        else -> R.drawable.ic_assignment_ind_white_48dp
    }
}