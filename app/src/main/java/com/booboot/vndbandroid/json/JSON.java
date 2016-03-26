package com.booboot.vndbandroid.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by od on 15/03/2016.
 */
public class JSON {
    public final static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
