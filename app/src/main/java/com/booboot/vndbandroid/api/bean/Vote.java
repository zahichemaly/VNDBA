package com.booboot.vndbandroid.api.bean;

import com.booboot.vndbandroid.R;

/**
 * Created by od on 15/03/2016.
 */
public class Vote {
    public static String toString(int vote) {
        return vote < 10 ? "Not on my votelist" : (vote / 10) + " (" + getName(vote / 10) + ")";
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
        else return "Other";
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
