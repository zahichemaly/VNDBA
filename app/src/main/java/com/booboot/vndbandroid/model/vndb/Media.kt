package com.booboot.vndbandroid.model.vndb

import java.util.*

data class Media(
        var medium: String = "",
        var qty: Int = 0
) {
    companion object {
        val FULL_TEXT: MutableMap<String, String> = mutableMapOf()

        init {
            FULL_TEXT.put("cd", "CD")
            FULL_TEXT.put("dvd", "DVD")
            FULL_TEXT.put("gdr", "GD-ROM")
            FULL_TEXT.put("blr", "Blu-ray disc")
            FULL_TEXT.put("flp", "Floppy")
            FULL_TEXT.put("mrt", "Cartridge")
            FULL_TEXT.put("mem", "Memory card")
            FULL_TEXT.put("umd", "UMD")
            FULL_TEXT.put("nod", "Nintendo Optical Disc")
            FULL_TEXT.put("in", "Internet download")
            FULL_TEXT.put("otc", "Other")
        }
    }
}
