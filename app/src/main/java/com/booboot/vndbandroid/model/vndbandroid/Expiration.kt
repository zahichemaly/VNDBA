package com.booboot.vndbandroid.model.vndbandroid

import com.chibatching.kotpref.KotprefModel

object Expiration : KotprefModel() {
    override val kotprefName: String = "VNDB_ANDROID_EXPIRATION"
    var tags by longPref()
    var traits by longPref()
}