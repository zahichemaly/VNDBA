package com.booboot.vndbandroid.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Display;

import com.booboot.vndbandroid.activity.EmptyActivity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by od on 03/04/2016.
 */
public class Utils {
    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static String booleanToString(boolean bool) {
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

    public static void openInBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    /**
     * Seamlessly recreates an activity, keeping its state and avoiding the blinking effect.
     * The trick is simple: another fake activity is launched and immediately killed, keeping the main thread busy
     * and preventing it from displaying a black screen while it's reloading our activity. The effect is even more
     * transparent if the fake activity launches itself twice.
     *
     * @param activity activity to recreate
     */
    public static void recreate(Activity activity) {
        Intent intent = new Intent(activity, EmptyActivity.class);
        activity.startActivity(intent);
        activity.recreate();
    }

    public static boolean isTablet(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Resources resources = activity.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return size.x / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT) > 1150;
        } else {
            return size.x / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT) > 760;
        }
    }
}
