package com.booboot.vndbandroid.db;

import android.content.Context;

import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Results;
import com.booboot.vndbandroid.json.JSON;
import com.booboot.vndbandroid.util.Callback;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by od on 15/03/2016.
 */
public class DB {
    public static Results results;

    public static void loadData(final Context context, final Callback successCallback) {
        VNDBServer.get("vnlist", "basic", "(uid = 0)", null, context, new Callback() {
            @Override
            public void config() {
                List<Integer> ids = new ArrayList<>();
                for (Item vnlistItem : results.getItems()) {
                    ids.add(vnlistItem.getVn());
                }
                try {
                    VNDBServer.get("vn", "basic,details", "(id = " + JSON.mapper.writeValueAsString(ids) + ")", null, context, new Callback() {
                        @Override
                        protected void config() {
                            DB.results = results;
                            successCallback.call();
                        }
                    }, Callback.errorCallback(context));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }, Callback.errorCallback(context));
    }
}
