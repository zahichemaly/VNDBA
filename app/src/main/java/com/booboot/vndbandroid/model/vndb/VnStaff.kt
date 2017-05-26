package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.R
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class VnStaff(var sid: Int = 0,
                   var aid: Int = 0,
                   var vid: Int = 0,
                   var name: String = "",
                   var role: String = "",
                   var original: String? = null,
                   var note: String? = null) {

    val icon: Int
        get() {
            when (role) {
                "director" -> return R.drawable.ic_theaters_white_48dp
                "songs" -> return R.drawable.ic_mic_white_48dp
                "music" -> return R.drawable.ic_audiotrack_white_48dp
                "scenario" -> return R.drawable.ic_text_format_white_48dp
                "art" -> return R.drawable.ic_palette_white_48dp
                "chardesign" -> return R.drawable.ic_brush_white_48dp
                "staff" -> return R.drawable.ic_assignment_ind_white_48dp
                else -> return R.drawable.ic_assignment_ind_white_48dp
            }
        }
}
