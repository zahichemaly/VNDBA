package com.booboot.vndbandroid.api.bean;

import android.content.Context;

import com.booboot.vndbandroid.json.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
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
