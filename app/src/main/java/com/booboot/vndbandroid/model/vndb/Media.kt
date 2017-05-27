package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class Media(var medium: String,
                 var qty: Int = 0) : VNDBCommand() {

    companion object {
        val FULL_TEXT = mapOf(
                "cd" to "CD",
                "dvd" to "DVD",
                "gdr" to "GD-ROM",
                "blr" to "Blu-ray disc",
                "flp" to "Floppy",
                "mrt" to "Cartridge",
                "mem" to "Memory card",
                "umd" to "UMD",
                "nod" to "Nintendo Optical Disc",
                "in" to "Internet download",
                "otc" to "Other"
        )
    }
}
