package com.booboot.vndbandroid.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by od on 25/04/2016.
 */
public class DateUtils {
    public static String getDate(String date) {
        if (date == null) {
            return "Unknown";
        } else try {
            Date released = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            return new SimpleDateFormat("d MMMM yyyy", Locale.US).format(released);
        } catch (ParseException e) {
            return date;
        }
    }
}
