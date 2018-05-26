package com.booboot.vndbandroid.model.vndbandroid

import com.chibatching.kotpref.KotprefModel

object Preferences : KotprefModel() {
    override val kotprefName: String = "VNDB_ANDROID_PREFS"
    var username by nullableStringPref(key = "USERNAME")
    var password by nullableStringPref(key = "PASSWORD")
    var loggedIn by booleanPref(key = "LOGGED_IN")
    var useCustomTabs by booleanPref(true, key = "IN_APP_BROWSER")
    var nsfw by booleanPref(false, key = "NSFW")
}