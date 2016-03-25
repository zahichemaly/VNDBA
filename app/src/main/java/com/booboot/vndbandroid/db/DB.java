package com.booboot.vndbandroid.db;

import android.content.Context;

import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.json.JSON;
import com.booboot.vndbandroid.util.Callback;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by od on 15/03/2016.
 */
public class DB {
    public static LinkedHashMap<Integer, Item> vnlist = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, Item> votelist = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, Item> wishlist = new LinkedHashMap<>();

    public static void loadData(final Context context, final Callback successCallback) {

        VNDBServer.get("vnlist", "basic", "(uid = 0)", null, context, new Callback() {
            @Override
            public void config() {
                final Map<Integer, Item> vnlistIds = new HashMap<>(), votelistIds = new HashMap<>(), wishlistIds = new HashMap<>();
                for (Item vnlistItem : results.getItems()) {
                    vnlistIds.put(vnlistItem.getVn(), vnlistItem);
                }

                VNDBServer.get("votelist", "basic", "(uid = 0)", null, context, new Callback() {
                    @Override
                    protected void config() {
                        for (Item votelistItem : results.getItems()) {
                            votelistIds.put(votelistItem.getVn(), votelistItem);
                        }

                        VNDBServer.get("wishlist", "basic", "(uid = 0)", null, context, new Callback() {
                            @Override
                            protected void config() {
                                for (Item wishlistItem : results.getItems()) {
                                    wishlistIds.put(wishlistItem.getVn(), wishlistItem);
                                }

                                Set<Integer> mergedIds = new HashSet<>(vnlistIds.keySet());
                                mergedIds.addAll(votelistIds.keySet());
                                mergedIds.addAll(wishlistIds.keySet());
                                try {
                                    VNDBServer.get("vn", "basic,details", "(id = " + JSON.mapper.writeValueAsString(mergedIds) + ")", null, context, new Callback() {
                                        @Override
                                        protected void config() {
                                            for (Item vn : results.getItems()) {
                                                Item vnlistItem = vnlistIds.get(vn.getId());
                                                Item votelistItem = votelistIds.get(vn.getId());
                                                Item wishlistItem = wishlistIds.get(vn.getId());

                                                if (vnlistItem != null) {
                                                    vn.setStatus(vnlistItem.getStatus());
                                                    DB.vnlist.put(vn.getId(), vn);
                                                }
                                                if (votelistItem != null) {
                                                    vn.setVote(votelistItem.getVote());
                                                    DB.votelist.put(vn.getId(), vn);
                                                }
                                                if (wishlistItem != null) {
                                                    vn.setPriority(wishlistItem.getPriority());
                                                    DB.wishlist.put(vn.getId(), vn);
                                                }
                                            }
                                            successCallback.call();
                                        }
                                    }, Callback.errorCallback(context));
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, Callback.errorCallback(context));
                    }
                }, Callback.errorCallback(context));
            }
        }, Callback.errorCallback(context));
    }
}
