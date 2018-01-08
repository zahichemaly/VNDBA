package com.booboot.vndbandroid.util

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.booboot.vndbandroid.App

@SuppressLint("CommitPrefEdits")
class PreferencesManager {
    companion object {
        private val PREFS_NAME = "VNDB_ANDROID_PREFS"

        private val prefs: SharedPreferences = App.instance.getSharedPreferences(PREFS_NAME, 0)

        fun username(): String? {
            return prefs.getString("USERNAME", null)
        }

        fun username(value: String) {
            with(prefs.edit()) {
                putString("USERNAME", value)
                commit()
            }
        }

        fun password(): String? {
            return prefs.getString("PASSWORD", null)
        }

        fun password(value: String) {
            with(prefs.edit()) {
                putString("PASSWORD", value)
                commit()
            }
        }

        fun credentialsSet(): Boolean {
            return prefs.getBoolean("CREDENTIALS_SET", false)
        }

        fun credentialsSet(value: Boolean) {
            with(prefs.edit()) {
                putBoolean("CREDENTIALS_SET", value)
                commit()
            }
        }

        fun useCustomTabs(): Boolean {
            return prefs.getBoolean("IN_APP_BROWSER", true)
        }

        fun useCustomTabs(value: Boolean) {
            with(prefs.edit()) {
                putBoolean("IN_APP_BROWSER", value)
                commit()
            }
        }
    }
}