package com.booboot.vndbandroid.bean.vndbandroid;

/**
 * Created by od on 15/03/2016.
 */
public class Status {
    public final static int UNKNOWN = 0;
    public final static int PLAYING = 1;
    public final static int FINISHED = 2;
    public final static int STALLED = 3;
    public final static int DROPPED = 4;
    public final static String DEFAULT = "Add to my VN list";

    public static String toString(int status) {
        switch (status) {
            case 0:
                return "Unknown";
            case 1:
                return "Playing";
            case 2:
                return "Finished";
            case 3:
                return "Stalled";
            case 4:
                return "Dropped";
            default:
                return DEFAULT;
        }
    }
}
