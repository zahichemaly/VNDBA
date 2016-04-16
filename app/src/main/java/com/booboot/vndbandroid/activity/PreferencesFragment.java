package com.booboot.vndbandroid.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.bean.Theme;
import com.booboot.vndbandroid.util.SettingsManager;

import java.util.HashMap;

public class PreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private static Preference.OnPreferenceChangeListener bindPreferenceSummaryToValueListener;
    public final static HashMap<String, Theme> THEMES = new HashMap<>();

    static {
        THEMES.put("0", new Theme(R.style.AppTheme, R.style.AppTheme_NoActionBar, R.drawable.bg_0));
        THEMES.put("1", new Theme(R.style.Theme1, R.style.Theme1_NoActionBar, R.drawable.bg_1));
        THEMES.put("2", new Theme(R.style.Theme2, R.style.Theme2_NoActionBar, R.drawable.bg_2));
        THEMES.put("3", new Theme(R.style.Theme3, R.style.Theme3_NoActionBar, R.drawable.bg_3));
        THEMES.put("4", new Theme(R.style.Theme4, R.style.Theme4_NoActionBar, R.drawable.bg_4));
        THEMES.put("5", new Theme(R.style.Theme5, R.style.Theme5_NoActionBar, R.drawable.bg_5));
        THEMES.put("6", new Theme(R.style.Theme6, R.style.Theme6_NoActionBar, R.drawable.bg_6));
        THEMES.put("7", new Theme(R.style.Theme7, R.style.Theme7_NoActionBar, R.drawable.bg_7));
        THEMES.put("8", new Theme(R.style.Theme8, R.style.Theme8_NoActionBar, R.drawable.bg_8));
        THEMES.put("9", new Theme(R.style.Theme9, R.style.Theme9_NoActionBar, R.drawable.bg_9));
        THEMES.put("10", new Theme(R.style.Theme10, R.style.Theme10_NoActionBar, R.drawable.bg_10));
        THEMES.put("11", new Theme(R.style.Theme11, R.style.Theme11_NoActionBar, R.drawable.bg_11));
        THEMES.put("12", new Theme(R.style.Theme12, R.style.Theme12_NoActionBar, R.drawable.bg_12));
        THEMES.put("13", new Theme(R.style.Theme13, R.style.Theme13_NoActionBar, R.drawable.bg_neon));
        THEMES.put("14", new Theme(R.style.Theme14, R.style.Theme14_NoActionBar, R.drawable.bg_13));
        THEMES.put("15", new Theme(R.style.Theme15, R.style.Theme15_NoActionBar, R.drawable.bg_14));
        THEMES.put("16", new Theme(R.style.Theme16, R.style.Theme16_NoActionBar, R.drawable.bg_15));
        THEMES.put("17", new Theme(R.style.Theme17, R.style.Theme17_NoActionBar, R.drawable.bg_16));
        THEMES.put("18", new Theme(R.style.Theme18, R.style.Theme18_NoActionBar, R.drawable.bg_17));
        THEMES.put("19", new Theme(R.style.Theme19, R.style.Theme19_NoActionBar, R.drawable.bg_18));
        THEMES.put("20", new Theme(R.style.Theme20, R.style.Theme20_NoActionBar, R.drawable.bg_19));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);
        bindPreferenceSummaryToValueListener = this;

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference("example_text"));
        bindPreferenceSummaryToValue(findPreference(getActivity().getString(R.string.pref_key_theme)));
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
        bindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
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
                int oldTheme = SettingsManager.getTheme(getActivity());
                Theme newTheme = THEMES.get(stringValue);
                if (newTheme == null) newTheme = THEMES.get("0");

                if (oldTheme != newTheme.getStyle()) {
                    SettingsManager.setTheme(getActivity(), newTheme.getStyle());
                    SettingsManager.setNoActionBarTheme(getActivity(), newTheme.getNoActionBarStyle());
                    SettingsManager.setWallpaper(getActivity(), newTheme.getWallpaper());
                    getActivity().recreate();
                }
            }
        } else {
            // For all other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }
}