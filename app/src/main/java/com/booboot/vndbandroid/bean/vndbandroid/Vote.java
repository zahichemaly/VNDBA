package com.booboot.vndbandroid.bean.vndbandroid;

import com.booboot.vndbandroid.R;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Created by od on 15/03/2016.
 */
public class Vote {
    public final static String DEFAULT = "Add a vote";
    private final static DecimalFormat VOTE_FORMAT = new DecimalFormat("#.#");

    public static double outOf10(int vote) {
        return vote / 10.0;
    }

    public static String toString(int vote) {
        if (vote < 1) return DEFAULT;
        return toShortString(vote) + " (" + getName(outOf10(vote)) + ")";
    }

    public static String toShortString(int vote) {
        return VOTE_FORMAT.format(outOf10(vote));
    }

    public static boolean isValid(String vote) {
        if (vote == null) return false;
        Pattern pattern = Pattern.compile("[1-9](\\.[0-9])?");
        return pattern.matcher(vote).matches();
    }

    public static String getName(double vote) {
        if (vote >= 10) return "masterpiece";
        if (vote >= 9) return "excellent";
        if (vote >= 8) return "very good";
        if (vote >= 7) return "good";
        if (vote >= 6) return "decent";
        if (vote >= 5) return "so-so";
        if (vote >= 4) return "weak";
        if (vote >= 3) return "bad";
        if (vote >= 2) return "awful";
        if (vote >= 1) return "worst ever";
        if (vote >= 0) return "Other";
        return DEFAULT;
    }

    public static Integer getImage(double vote) {
        if (vote >= 8) return R.drawable.score_green;
        if (vote >= 7) return R.drawable.score_light_green;
        if (vote >= 6) return R.drawable.score_yellow;
        if (vote >= 4) return R.drawable.score_light_orange;
        if (vote >= 3) return R.drawable.score_orange;
        else return R.drawable.score_red;
    }
}
