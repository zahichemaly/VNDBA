package com.booboot.vndbandroid.db;

import android.content.Context;

import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Results;
import com.booboot.vndbandroid.json.JSON;
import com.booboot.vndbandroid.util.Callback;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by od on 15/03/2016.
 */
public class DB {
    public static Results results;

    public static void loadData(final Context context, final Callback successCallback) {
        VNDBServer.get("vnlist", "basic", "(uid = 0)", null, context, new Callback() {
            @Override
            public void config() {
                final Map<Integer, Item> ids = new HashMap<>();
                for (Item vnlistItem : results.getItems()) {
                    ids.put(vnlistItem.getVn(), vnlistItem);
                }
                try {
                    VNDBServer.get("vn", "basic,details", "(id = " + JSON.mapper.writeValueAsString(ids.keySet()) + ")", null, context, new Callback() {
                        @Override
                        protected void config() {
                            for (Item vn : results.getItems()) {
                                vn.setStatus(ids.get(vn.getId()).getStatus());
                            }
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
