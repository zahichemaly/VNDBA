package com.booboot.vndbandroid.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by od on 12/03/2016.
 */
public class SettingsManager {
    public static final String PREFS_NAME = "VNDB_ANDROID_PREFS";
    public static SharedPreferences settings;
    public static SharedPreferences.Editor editor;

    public SettingsManager(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
    }

    public static void setUsername(Context context, String username) {
        new SettingsManager(context);
        editor.putString("USERNAME", username);
        editor.commit();
    }

    public static void setPassword(Context context, String password) {
        new SettingsManager(context);
        editor.putString("PASSWORD", password);
        editor.commit();
    }

    public static String getUsername(Context context) {
        new SettingsManager(context);
        return settings.getString("USERNAME", null);
    }

    public static String getPassword(Context context) {
        new SettingsManager(context);
        return settings.getString("PASSWORD", null);
    }
}
