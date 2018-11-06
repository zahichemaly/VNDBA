package com.booboot.vndbandroid.ui.preferences

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.toBooleanOrFalse
import com.booboot.vndbandroid.model.vndbandroid.NO
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.YES

class PreferencesFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = Preferences.kotprefName
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        addPreferencesFromResource(R.xml.app_preferences)

        /* Replicating indirect preferences from Preferences to the UI */
        val prefGdprCrashlytics = preferenceScreen.findPreference(getString(R.string.pref_key_gdpr_crashlytics)) as? SwitchPreference
        prefGdprCrashlytics?.isChecked = Preferences.gdprCrashlytics == YES

        for (i in 0 until preferenceScreen.preferenceCount) {
            preferenceScreen.getPreference(i).onPreferenceChangeListener = this
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        when (preference.key) {
            getString(R.string.pref_key_night_mode) -> {
                val value = newValue.toString().toInt()
                if (Preferences.nightMode != value) {
                    AppCompatDelegate.setDefaultNightMode(value)
                    Preferences.nightMode = value
                    activity?.recreate()
                }
            }
            getString(R.string.pref_key_gdpr_crashlytics) -> {
                val value = newValue.toString().toBooleanOrFalse()
                Preferences.gdprCrashlytics = if (value) YES else NO
            }
        }
        return true
    }
}