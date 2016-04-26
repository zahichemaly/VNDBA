package com.booboot.vndbandroid.util;

import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Created by od on 03/04/2016.
 */
public class StringUtils {
    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static String toString(boolean bool) {
        return bool ? "Yes" : "No";
    }

    public static String capitalize(String s) {
        if (s == null) return null;
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        if (s.length() > 1) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return "";
    }
}
