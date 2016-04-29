package com.booboot.vndbandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.booboot.vndbandroid.R;

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

    public static String getUsername(Context context) {
        new SettingsManager(context);
        return settings.getString("USERNAME", null);
    }

    public static void setUsername(Context context, String username) {
        new SettingsManager(context);
        editor.putString("USERNAME", username);
        editor.commit();
    }

    public static String getPassword(Context context) {
        new SettingsManager(context);
        return settings.getString("PASSWORD", null);
    }

    public static void setPassword(Context context, String password) {
        new SettingsManager(context);
        editor.putString("PASSWORD", password);
        editor.commit();
    }

    public static int getTheme(Context context) {
        new SettingsManager(context);
        return settings.getInt("THEME", R.style.AppTheme);
    }

    public static void setTheme(Context context, int theme) {
        new SettingsManager(context);
        editor.putInt("THEME", theme);
        editor.commit();
    }

    public static int getNoActionBarTheme(Context context) {
        new SettingsManager(context);
        return settings.getInt("NO_ACTION_BAR_THEME", R.style.AppTheme_NoActionBar);
    }

    public static void setNoActionBarTheme(Context context, int theme) {
        new SettingsManager(context);
        editor.putInt("NO_ACTION_BAR_THEME", theme);
        editor.commit();
    }

    public static int getWallpaper(Context context) {
        new SettingsManager(context);
        return settings.getInt("WALLPAPER", R.drawable.bg_0);
    }

    public static void setWallpaper(Context context, int wallpaper) {
        new SettingsManager(context);
        editor.putInt("WALLPAPER", wallpaper);
        editor.commit();
    }

    public static int getSort(Context context) {
        new SettingsManager(context);
        return settings.getInt("SORT", 0);
    }

    public static void setSort(Context context, int sort) {
        new SettingsManager(context);
        editor.putInt("SORT", sort);
        editor.commit();
    }

    public static boolean getReverseSort(Context context) {
        new SettingsManager(context);
        return settings.getBoolean("REVERSE_SORT", false);
    }

    public static void setReverseSort(Context context, boolean reverse) {
        new SettingsManager(context);
        editor.putBoolean("REVERSE_SORT", reverse);
        editor.commit();
    }

    public static int getSpoilerLevel(Context context) {
        new SettingsManager(context);
        return settings.getInt("SPOILER_LEVEL", 0);
    }

    public static void setSpoilerLevel(Context context, int spoiler) {
        new SettingsManager(context);
        editor.putInt("SPOILER_LEVEL", spoiler);
        editor.commit();
    }

    public static boolean getSpoilerCompleted(Context context) {
        new SettingsManager(context);
        return settings.getBoolean("SPOILER_COMPLETED", true);
    }

    public static void setSpoilerCompleted(Context context, boolean ok) {
        new SettingsManager(context);
        editor.putBoolean("SPOILER_COMPLETED", ok);
        editor.commit();
    }

    public static boolean getNSFW(Context context) {
        new SettingsManager(context);
        return settings.getBoolean("NSFW", false);
    }

    public static void setNSFW(Context context, boolean ok) {
        new SettingsManager(context);
        editor.putBoolean("NSFW", ok);
        editor.commit();
    }
}
