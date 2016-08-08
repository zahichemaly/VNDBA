package com.booboot.vndbandroid.api;

import com.booboot.vndbandroid.bean.vnstat.VNStatResults;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.JSON;

import java.io.IOException;
import java.net.URL;

/**
 * Created by od on 27/07/2016.
 */
public class VNStatServer {
    public final static String URL = "https://beta.vnstat.net/api/novel/";

    public static void get(final String flags, final int vnId, final Callback successCallback, final Callback errorCallback) {
        new Thread() {
            public void run() {
                try {
                    VNStatResults response = JSON.mapper.readValue(new URL(URL + vnId + "/" + flags), VNStatResults.class);

                    if (response.isSuccess()) {
                        successCallback.vnStatResults = response.getResult();
                        successCallback.call();
                    } else {
                        errorCallback.call();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    errorCallback.message = "An error occurred during the connection to the server. Please try again later.";
                    errorCallback.call();
                }
            }
        }.start();
    }
}
