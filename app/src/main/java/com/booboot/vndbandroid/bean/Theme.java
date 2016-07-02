package com.booboot.vndbandroid.bean;

/**
 * Created by od on 16/04/2016.
 */
public class Theme {
    private int style;
    private int noActionBarStyle;
    private int wallpaper;

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
