package com.booboot.vndbandroid.model.vndbandroid

import androidx.appcompat.app.AppCompatDelegate
import com.booboot.vndbandroid.R
import com.chibatching.kotpref.KotprefModel

object Preferences : KotprefModel() {
    override val kotprefName: String = "VNDB_ANDROID_PREFS"
    var username by nullableStringPref(key = "USERNAME")
    var password by nullableStringPref(key = "PASSWORD")
    var loggedIn by booleanPref(key = "LOGGED_IN")
    var useCustomTabs by booleanPref(true, key = context.getString(R.string.pref_key_browser))
    var nsfw by booleanPref(false, key = context.getString(R.string.pref_key_nsfw))
    var nightMode by intPref(AppCompatDelegate.MODE_NIGHT_YES, key = "NIGHT_MODE")
}