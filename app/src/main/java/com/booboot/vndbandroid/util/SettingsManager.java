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
        return settings.getString("USERNAME", "");
    }

    public static void setUsername(Context context, String username) {
        new SettingsManager(context);
        editor.putString("USERNAME", username);
        editor.commit();
    }

    public static int getUserId(Context context) {
        new SettingsManager(context);
        return settings.getInt("USERID", -1);
    }

    public static void setUserId(Context context, int userId) {
        new SettingsManager(context);
        editor.putInt("USERID", userId);
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

    public static String getTheme(Context context) {
        new SettingsManager(context);
        return settings.getString("THEME_INDEX", "0");
    }

    public static void setTheme(Context context, String theme) {
        new SettingsManager(context);
        editor.putString("THEME_INDEX", theme);
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

    public static boolean getInAppBrowser(Context context) {
        new SettingsManager(context);
        return settings.getBoolean("IN_APP_BROWSER", true);
    }

    public static void setInAppBrowser(Context context, boolean ok) {
        new SettingsManager(context);
        editor.putBoolean("IN_APP_BROWSER", ok);
        editor.commit();
    }

    public static boolean getCoverBackground(Context context) {
        new SettingsManager(context);
        return settings.getBoolean("COVER_BACKGROUND", true);
    }

    public static void setCoverBackground(Context context, boolean ok) {
        new SettingsManager(context);
        editor.putBoolean("COVER_BACKGROUND", ok);
        editor.commit();
    }

    public static boolean getHideRecommendationsInWishlist(Context context) {
        new SettingsManager(context);
        return settings.getBoolean("HIDE_RECOMMENDATIONS_IN_WISHLIST", false);
    }

    public static void setHideRecommendationsInWishlist(Context context, boolean ok) {
        new SettingsManager(context);
        editor.putBoolean("HIDE_RECOMMENDATIONS_IN_WISHLIST", ok);
        editor.commit();
    }
}
