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
    var nsfw by booleanPref(key = context.getString(R.string.pref_key_nsfw))
    var nightMode by intPref(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, "NIGHT_MODE") // Don't use R.string.pref_key_night_mode because not an int!
    var sort by longPref(SORT_ID, key = "SORT")
    var reverseSort by booleanPref(false, key = "REVERSE_SORT")
    var gdprCrashlytics by intPref(NOT_SET, key = "GDPR_CRASHLYTICS")
    var loginHelpSeen by booleanPref(false, key = "LOGIN_HELP_SEEN")
    var shouldResetPreferences by booleanPref(true, key = "SHOULD_RESET_PREFERENCES")
    var useSharedTransitions by booleanPref(true, key = context.getString(R.string.pref_key_shared_transitions))

    val selectedFilters by stringSetPref(key = "SELECTED_FILTERS")
    var resetFiltersAtStartup by booleanPref(true, key = context.getString(R.string.pref_key_reset_filters_at_startup))
}