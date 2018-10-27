package com.booboot.vndbandroid.ui.preferences

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.model.vndbandroid.Preferences

class PreferencesFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = Preferences.kotprefName
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        addPreferencesFromResource(R.xml.app_preferences)

        for (i in 0 until preferenceScreen.preferenceCount) {
            preferenceScreen.getPreference(i).onPreferenceChangeListener = this
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (preference is ListPreference) {
            val value = newValue.toString().toInt()

            when (preference.key) {
                getString(R.string.pref_key_night_mode) -> {
                    if (Preferences.nightMode != value) {
                        AppCompatDelegate.setDefaultNightMode(value)
                        Preferences.nightMode = value
                        activity?.recreate()
                    }
                }
            }
        }
        return true
    }
}