package com.booboot.vndbandroid.db;

import android.content.Context;

import com.booboot.vndbandroid.api.VNDBServer;
import com.booboot.vndbandroid.api.bean.DbStats;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Options;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.IPredicate;
import com.booboot.vndbandroid.util.JSON;
import com.booboot.vndbandroid.util.Predicate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by od on 15/03/2016.
 */
public class Cache {
    public static LinkedHashMap<Integer, Item> vnlist = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, Item> votelist = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, Item> wishlist = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, List<Item>> characters = new LinkedHashMap<>();

    public final static String VN_FLAGS = "basic,details,screens,tags,stats,relations";
    public final static String CHARACTER_FLAGS = "basic,details,meas,traits,vns";

    public final static String VNLIST_CACHE = "vnlist.data";
    public final static String VOTELIST_CACHE = "votelist.data";
    public final static String WISHLIST_CACHE = "wishlist.data";
    public final static String CHARACTERS_CACHE = "characters.data";
    public final static String DBSTATS_CACHE = "dbstats.data";

    public static DbStats dbstats;
    private static String mergedIdsString;

    public static void loadData(final Context context, final Callback successCallback) {
        Cache.vnlist.clear();
        VNDBServer.get("vnlist", "basic", "(uid = 0)", Options.create(true, true), context, new Callback() {
            @Override
            public void config() {
                final Map<Integer, Item> vnlistIds = new HashMap<>(), votelistIds = new HashMap<>(), wishlistIds = new HashMap<>();
                for (Item vnlistItem : results.getItems()) {
                    vnlistIds.put(vnlistItem.getVn(), vnlistItem);
                }

                Cache.votelist.clear();
                VNDBServer.get("votelist", "basic", "(uid = 0)", Options.create(true, true), context, new Callback() {
                    @Override
                    protected void config() {
                        for (Item votelistItem : results.getItems()) {
                            votelistIds.put(votelistItem.getVn(), votelistItem);
                        }

                        Cache.wishlist.clear();
                        VNDBServer.get("wishlist", "basic", "(uid = 0)", Options.create(true, true), context, new Callback() {
                            @Override
                            protected void config() {
                                for (Item wishlistItem : results.getItems()) {
                                    wishlistIds.put(wishlistItem.getVn(), wishlistItem);
                                }

                                Set<Integer> mergedIds = new HashSet<>(vnlistIds.keySet());
                                mergedIds.addAll(votelistIds.keySet());
                                mergedIds.addAll(wishlistIds.keySet());
                                try {
                                    mergedIdsString = JSON.mapper.writeValueAsString(mergedIds);
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                                VNDBServer.get("vn", VN_FLAGS, "(id = " + mergedIdsString + ")", Options.create(true, true), context, new Callback() {
                                    @Override
                                    protected void config() {
                                        for (Item vn : results.getItems()) {
                                            Item vnlistItem = vnlistIds.get(vn.getId());
                                            Item votelistItem = votelistIds.get(vn.getId());
                                            Item wishlistItem = wishlistIds.get(vn.getId());

                                            if (vnlistItem != null) {
                                                vn.setStatus(vnlistItem.getStatus());
                                                Cache.vnlist.put(vn.getId(), vn);
                                            }
                                            if (votelistItem != null) {
                                                vn.setVote(votelistItem.getVote());
                                                Cache.votelist.put(vn.getId(), vn);
                                            }
                                            if (wishlistItem != null) {
                                                vn.setPriority(wishlistItem.getPriority());
                                                Cache.wishlist.put(vn.getId(), vn);
                                            }
                                        }

                                        saveToCache(context, VNLIST_CACHE, vnlist);
                                        saveToCache(context, VOTELIST_CACHE, votelist);
                                        saveToCache(context, WISHLIST_CACHE, wishlist);
                                        successCallback.call();
                                    }
                                }, Callback.errorCallback(context));
                            }
                        }, Callback.errorCallback(context));
                    }
                }, Callback.errorCallback(context));
            }
        }, Callback.errorCallback(context));
    }

    public static void saveToCache(Context context, String filename, Object object) {
        File file = new File(context.getFilesDir(), filename);
        try {
            JSON.mapper.writeValue(file, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean loadFromCache(Context context) {
        File vnlistFile = new File(context.getFilesDir(), VNLIST_CACHE);
        File votelistFile = new File(context.getFilesDir(), VOTELIST_CACHE);
        File wishlistFile = new File(context.getFilesDir(), WISHLIST_CACHE);
        File charactersFile = new File(context.getFilesDir(), CHARACTERS_CACHE);

        if (!vnlistFile.exists() || !votelistFile.exists() || !wishlistFile.exists())
            return false;

        try {
            vnlist = JSON.mapper.readValue(vnlistFile, new TypeReference<LinkedHashMap<Integer, Item>>() {
            });
            votelist = JSON.mapper.readValue(votelistFile, new TypeReference<LinkedHashMap<Integer, Item>>() {
            });
            wishlist = JSON.mapper.readValue(wishlistFile, new TypeReference<LinkedHashMap<Integer, Item>>() {
            });
            if (charactersFile.exists()) {
                characters = JSON.mapper.readValue(charactersFile, new TypeReference<LinkedHashMap<Integer, List<Item>>>() {
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void loadStatsFromCache(Context context) {
        File dbstatsFile = new File(context.getFilesDir(), DBSTATS_CACHE);
        if (dbstatsFile.exists()) {
            try {
                dbstats = JSON.mapper.readValue(dbstatsFile, DbStats.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearCache(Context context) {
        File vnlistFile = new File(context.getFilesDir(), VNLIST_CACHE);
        File votelistFile = new File(context.getFilesDir(), VOTELIST_CACHE);
        File wishlistFile = new File(context.getFilesDir(), WISHLIST_CACHE);
        File charactersFile = new File(context.getFilesDir(), CHARACTERS_CACHE);

        if (vnlistFile.exists()) vnlistFile.delete();
        if (votelistFile.exists()) votelistFile.delete();
        if (wishlistFile.exists()) wishlistFile.delete();
        if (charactersFile.exists()) charactersFile.delete();
    }

    public static void loadStats(final Context context, final Callback successCallback, boolean forceRefresh) {
        if (Cache.dbstats != null && !forceRefresh) {
            successCallback.call();
            return;
        }

        VNDBServer.dbstats(new Callback() {
            @Override
            protected void config() {
                Cache.dbstats = dbstats;
                saveToCache(context, DBSTATS_CACHE, dbstats);
                successCallback.call();
            }
        }, Callback.errorCallback(context));
    }

    public static int getStatusNumber(final int status) {
        return Predicate.filter(vnlist.values(), new IPredicate<Item>() {
            @Override
            public boolean apply(Item element) {
                return element.getStatus() == status;
            }
        }).size();
    }

    public static int getWishNumber(final int priority) {
        return Predicate.filter(wishlist.values(), new IPredicate<Item>() {
            @Override
            public boolean apply(Item element) {
                return element.getPriority() == priority;
            }
        }).size();
    }

    public static int getVoteNumber(final int vote) {
        return Predicate.filter(votelist.values(), new IPredicate<Item>() {
            @Override
            public boolean apply(Item element) {
                return element.getVote() / 10 == vote || element.getVote() / 10 == vote - 1;
            }
        }).size();
    }

}