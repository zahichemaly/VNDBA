package com.booboot.vndbandroid.model.vndbandroid;

import com.booboot.vndbandroid.model.vndb.VNDBCommand;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends VNDBCommand {
    public final static LinkedHashMap<String, String> CATEGORIES = new LinkedHashMap<>();
    public static List<String> CATEGORIES_KEYS;

    static {
        CATEGORIES.put("tech", "Technical");
        CATEGORIES.put("cont", "Content");
        CATEGORIES.put("ero", "Sexual content");

        CATEGORIES.put("director", "Director");
        CATEGORIES.put("songs", "Vocals");
        CATEGORIES.put("music", "Composer");
        CATEGORIES.put("scenario", "Scenario");
        CATEGORIES.put("art", "Artist");
        CATEGORIES.put("staff", "Staff");

        CATEGORIES_KEYS = new ArrayList<>(CATEGORIES.keySet());
    }
}
