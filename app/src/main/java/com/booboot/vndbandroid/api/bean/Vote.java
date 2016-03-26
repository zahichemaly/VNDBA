package com.booboot.vndbandroid.api.bean;

/**
 * Created by od on 15/03/2016.
 */
public class Vote {
    public static String toString(int vote) {
        return vote < 10 ? "Not voted yet" : (vote / 10) + " / 10";
    }
}
