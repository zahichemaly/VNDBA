package com.booboot.vndbandroid.api.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by od on 12/03/2016.
 */
public abstract class VNDBCommand implements Serializable {
    private final static HashMap<String, Class> CLASSES = new HashMap<>();

    static {
        CLASSES.put("login", Login.class);
        CLASSES.put("error", Error.class);
        CLASSES.put("results", Results.class);
    }

    public static Class getClass(String command) {
        return CLASSES.get(command);
    }
}
