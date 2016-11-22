package com.booboot.vndbandroid.bean.vndbandroid;

/**
 * Created by od on 15/03/2016.
 */
public class Priority {
    public final static int HIGH = 0;
    public final static int MEDIUM = 1;
    public final static int LOW = 2;
    public final static int BLACKLIST = 3;
    public final static String DEFAULT = "Add to my wishlist";

    public static String toString(int priority) {
        switch (priority) {
            case 0:
                return "High";
            case 1:
                return "Medium";
            case 2:
                return "Low";
            case 3:
                return "Blacklist";
            default:
                return DEFAULT;
        }
    }

    public static String toShortString(int priority) {
        switch (priority) {
            case 0:
                return "H";
            case 1:
                return "M";
            case 2:
                return "L";
            case 3:
                return "B";
            default:
                return "-";
        }    }
}
