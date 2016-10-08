package com.booboot.vndbandroid.bean.vndbandroid;

import com.booboot.vndbandroid.R;

import java.util.HashMap;

/**
 * Created by od on 16/04/2016.
 */
public class Theme {
    private int style;
    private int noActionBarStyle;
    private int wallpaper;

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

    public Theme(int style, int noActionBarStyle, int wallpaper) {
        this.style = style;
        this.noActionBarStyle = noActionBarStyle;
        this.wallpaper = wallpaper;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(int wallpaper) {
        this.wallpaper = wallpaper;
    }

    public int getNoActionBarStyle() {
        return noActionBarStyle;
    }

    public void setNoActionBarStyle(int noActionBarStyle) {
        this.noActionBarStyle = noActionBarStyle;
    }
}
