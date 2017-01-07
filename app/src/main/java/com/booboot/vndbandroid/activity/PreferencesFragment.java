package com.booboot.vndbandroid.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.bean.vndbandroid.Theme;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;

public class PreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private static Preference.OnPreferenceChangeListener bindPreferenceSummaryToValueListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);
        Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.preferences));
        bindPreferenceSummaryToValueListener = this;

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference(getActivity().getString(R.string.pref_key_spoiler_completed)));
        bindPreferenceSummaryToValue(findPreference(getActivity().getString(R.string.pref_key_theme)));
        bindPreferenceSummaryToValue(findPreference(getActivity().getString(R.string.pref_key_spoiler)));
        bindPreferenceSummaryToValue(findPreference(getActivity().getString(R.string.pref_key_nsfw)));
        bindPreferenceSummaryToValue(findPreference(getActivity().getString(R.string.pref_key_browser)));
        bindPreferenceSummaryToValue(findPreference(getActivity().getString(R.string.pref_key_cover_background)));
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #bindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(bindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's current value.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        Object newValue;
        if (preference instanceof SwitchPreference)
            newValue = sharedPreferences.getBoolean(preference.getKey(), false);
        else newValue = sharedPreferences.getString(preference.getKey(), "");

        bindPreferenceSummaryToValueListener.onPreferenceChange(preference, newValue);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

            if (listPreference.getKey().equals(getActivity().getString(R.string.pref_key_theme))) {
                String oldTheme = SettingsManager.getTheme(getActivity());
                String newTheme = Theme.THEMES.get(stringValue) != null ? stringValue : "0";

                if (!oldTheme.equals(newTheme)) {
                    SettingsManager.setTheme(getActivity(), stringValue);
                    Utils.recreate(getActivity());
                }
            } else if (listPreference.getKey().equals(getActivity().getString(R.string.pref_key_spoiler))) {
                SettingsManager.setSpoilerLevel(getActivity(), Integer.valueOf(stringValue));
            }
        } else {
            if (preference.getKey().equals(getActivity().getString(R.string.pref_key_spoiler_completed))) {
                SettingsManager.setSpoilerCompleted(getActivity(), Boolean.parseBoolean(stringValue));
            } else if (preference.getKey().equals(getActivity().getString(R.string.pref_key_nsfw))) {
                SettingsManager.setNSFW(getActivity(), Boolean.parseBoolean(stringValue));
            } else if (preference.getKey().equals(getActivity().getString(R.string.pref_key_browser))) {
                SettingsManager.setInAppBrowser(getActivity(), Boolean.parseBoolean(stringValue));
            } else if (preference.getKey().equals(getActivity().getString(R.string.pref_key_cover_background))) {
                SettingsManager.setCoverBackground(getActivity(), Boolean.parseBoolean(stringValue));
            }
        }
        return true;
    }
}