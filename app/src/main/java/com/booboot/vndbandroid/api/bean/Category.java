package com.booboot.vndbandroid.api.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends VNDBCommand {
    public final static Map<String, String> CATEGORIES = new HashMap<>();

    static {
        CATEGORIES.put("tech", "Technical");
        CATEGORIES.put("cont", "Content");
        CATEGORIES.put("ero", "Sexual content");
    }
}
