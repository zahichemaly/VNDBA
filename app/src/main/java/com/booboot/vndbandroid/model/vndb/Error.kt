package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Error(var id: String = "",
                 var msg: String = "",
                 var type: String = "",
                 var minwait: Double = 0.toDouble(),
                 var fullwait: Double = 0.toDouble()) : VNDBCommand() {

    fun fullMessage(): String = when (id) {
        "throttled" -> "VNDB.org is too busy to fulfill your request now, so your lists may not be up-to-date. Please wait a bit and try again."
        else -> id + " : " + msg
    }
}