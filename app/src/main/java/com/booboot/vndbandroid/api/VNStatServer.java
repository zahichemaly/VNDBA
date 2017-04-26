package com.booboot.vndbandroid.api;

import com.booboot.vndbandroid.model.vnstat.VNStatResults;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.JSON;
import com.booboot.vndbandroid.util.Utils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by od on 27/07/2016.
 */
public class VNStatServer {
    public final static String HOST = "vnstat.net";
    public final static String URL = "https://vnstat.net/api/";

    public static void get(final String type, final String flags, final int id, final Callback successCallback, final Callback errorCallback) {
        new Thread() {
            public void run() {
                try {
                    VNStatResults response = JSON.mapper.readValue(new URL(URL + type + "/" + id + "/" + flags), VNStatResults.class);

                    if (response.isSuccess()) {
                        successCallback.vnStatResults = response.getResult();
                        successCallback.call();
                    } else {
                        if (response.getMessage() != null)
                            errorCallback.message = response.getMessage();
                        errorCallback.call();
                    }
                } catch (IOException e) {
                    Utils.processException(e);
                    errorCallback.message = "Unable to reach the server " + HOST + ". Please try again later.";
                    errorCallback.call();
                }
            }
        }.start();
    }
}
