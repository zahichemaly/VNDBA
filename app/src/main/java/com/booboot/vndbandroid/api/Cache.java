package com.booboot.vndbandroid.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.activity.StaffActivity;
import com.booboot.vndbandroid.activity.VNDetailsActivity;
import com.booboot.vndbandroid.activity.VNTypeFragment;
import com.booboot.vndbandroid.model.vndb.CharacterVoiced;
import com.booboot.vndbandroid.model.vndb.DbStats;
import com.booboot.vndbandroid.model.vndb.Item;
import com.booboot.vndbandroid.model.vndb.Options;
import com.booboot.vndbandroid.model.vndbandroid.CacheItem;
import com.booboot.vndbandroid.model.vndbandroid.VNlistItem;
import com.booboot.vndbandroid.model.vndbandroid.VotelistItem;
import com.booboot.vndbandroid.model.vndbandroid.WishlistItem;
import com.booboot.vndbandroid.model.vnstat.SimilarNovel;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.JSON;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Created by od on 15/03/2016.
 */
public class Cache {
    public static LinkedHashMap<Integer, VNlistItem> vnlist = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, VotelistItem> votelist = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, WishlistItem> wishlist = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, Item> vns = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, List<Item>> characters = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, Item> staff = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, List<Item>> releases = new LinkedHashMap<>();
    public static LinkedHashMap<Integer, List<SimilarNovel>> similarNovels = new LinkedHashMap<>();

    public final static String VN_FLAGS = "basic,details,screens,tags,stats,relations,anime,staff";
    public final static String CHARACTER_FLAGS = "basic,details,meas,traits,vns,voiced";
    public final static String RELEASE_FLAGS = "basic,details,producers";
    public final static String STAFF_FLAGS = "basic,details,vns,voiced";

    private final static String DBSTATS_CACHE = "dbstats.data";
    public static boolean loadedFromCache = false;

    public final static String[] SORT_OPTIONS = new String[]{
            "ID",
            "Title",
            "Release date",
            "Length",
            "Popularity",
            "Rating",
            "Status",
            "Vote",
            "Wish"
    };

    public static DbStats dbstats;
    private static String mergedIdsString;
    public static boolean shouldRefreshView;

    public static boolean pipeliningError;
    public static CountDownLatch countDownLatch;

    public static void loadData(final Context context, final Callback successCallback, final Callback errorCallback) {
        new Thread() {
            public void run() {
                shouldRefreshView = false;
                final Map<Integer, Item> vnlistIds = new HashMap<>(), votelistIds = new HashMap<>(), wishlistIds = new HashMap<>();

                /* Initializing multi-threading variables */
                pipeliningError = false;
                countDownLatch = new CountDownLatch(3);

                VNDBServer.get("vnlist", "basic", "(uid = 0)", Options.create(1, 100, null, false, true, 0), 0, context, new Callback() {
                    @Override
                    public void config() {
                        for (Item vnlistItem : results.getItems()) {
                            vnlistIds.put(vnlistItem.getVn(), vnlistItem);
                        }
                        if (countDownLatch != null) countDownLatch.countDown();
                    }
                }, errorCallback);

                VNDBServer.get("votelist", "basic", "(uid = 0)", Options.create(1, 100, null, false, true, 0), 1, context, new Callback() {
                    @Override
                    protected void config() {
                        for (Item votelistItem : results.getItems()) {
                            votelistIds.put(votelistItem.getVn(), votelistItem);
                        }
                        if (countDownLatch != null) countDownLatch.countDown();
                    }
                }, errorCallback);

                VNDBServer.get("wishlist", "basic", "(uid = 0)", Options.create(1, 100, null, false, true, 0), 2, context, new Callback() {
                    @Override
                    protected void config() {
                        for (Item wishlistItem : results.getItems()) {
                            wishlistIds.put(wishlistItem.getVn(), wishlistItem);
                        }
                        if (countDownLatch != null) countDownLatch.countDown();
                    }
                }, errorCallback);

                try {
                    countDownLatch.await();
                } catch (InterruptedException exception) {
                    Utils.processException(exception);
                    errorCallback.message = "An unexpected error occurred while loading your lists. Please try again later.";
                    errorCallback.call();
                    return;
                }

                countDownLatch = null;
                if (pipeliningError) return;

                Set<Integer> mergedIds = new HashSet<>(vnlistIds.keySet());
                mergedIds.addAll(votelistIds.keySet());
                mergedIds.addAll(wishlistIds.keySet());
                mergedIdsString = TextUtils.join(",", mergedIds);
                DB.startupClean(context, mergedIdsString, vnlistIds.keySet(), votelistIds.keySet(), wishlistIds.keySet());

                SettingsManager.setEmptyAccount(context, mergedIds.isEmpty());
                if (!shouldSendGetVn(context, vnlistIds, votelistIds, wishlistIds, mergedIds) || mergedIds.isEmpty()) {
                    successCallback.call();
                    return;
                }

                mergedIdsString = TextUtils.join(",", mergedIds);
                int numberOfPages = (int) Math.ceil(mergedIds.size() * 1.0 / 25);
                VNDBServer.get("vn", VN_FLAGS, "(id = [" + mergedIdsString + "])", Options.create(true, numberOfPages), 0, context, new Callback() {
                    @Override
                    protected void config() {
                        for (Item vn : results.getItems()) {
                            Item vnlistItem = vnlistIds.get(vn.getId());
                            Item votelistItem = votelistIds.get(vn.getId());
                            Item wishlistItem = wishlistIds.get(vn.getId());

                            if (vnlistItem != null) {
                                VNlistItem item = new VNlistItem();
                                item.setVn(vn.getId());
                                item.setStatus(vnlistItem.getStatus());
                                item.setNotes(vnlistItem.getNotes());
                                item.setAdded(vnlistItem.getAdded());
                                Cache.vnlist.put(vn.getId(), item);
                            }
                            if (votelistItem != null) {
                                VotelistItem item = new VotelistItem();
                                item.setVn(vn.getId());
                                item.setVote(votelistItem.getVote());
                                item.setAdded(votelistItem.getAdded());
                                Cache.votelist.put(vn.getId(), item);
                            }
                            if (wishlistItem != null) {
                                WishlistItem item = new WishlistItem();
                                item.setVn(vn.getId());
                                item.setPriority(wishlistItem.getPriority());
                                item.setAdded(wishlistItem.getAdded());
                                Cache.wishlist.put(vn.getId(), item);
                            }

                            Cache.vns.put(vn.getId(), vn);
                        }

                        sortAll(context);
                        DB.saveVnlist(context, true, false);
                        DB.saveVotelist(context, false, false);
                        DB.saveWishlist(context, false, false);
                        DB.saveVNs(context, false, true);
                        loadedFromCache = true;

                        shouldRefreshView = true;
                        successCallback.call();
                    }
                }, errorCallback);
            }
        }.start();
    }

    /**
     * @return true if the up-to-date lists (directly fetched from the API) are different from the cache content.
     */
    private static boolean shouldSendGetVn(Context context, Map<Integer, Item> vnlistIds, Map<Integer, Item> votelistIds, Map<Integer, Item> wishlistIds, Set<Integer> mergedIds) {
        /* 1 - Checking for VNs that have been removed or modified overtime */
        boolean vnlistHasChanged = false;
        for (int id : new HashSet<>(vnlist.keySet())) {
            Item vnlistItem = vnlistIds.get(id);
            if (vnlistItem == null) {
                vnlist.remove(id);
                vnlistHasChanged = true;
            } else if (vnlist.get(id) != null) {
                if (vnlistItemHasChanged(vnlist.get(id), vnlistItem.getStatus(), vnlistItem.getNotes())) {
                    vnlist.get(id).setStatus(vnlistItem.getStatus());
                    vnlist.get(id).setNotes(vnlistItem.getNotes());
                    vnlistHasChanged = true;
                }
            }
        }
        boolean votelistHasChanged = false;
        for (int id : new HashSet<>(votelist.keySet())) {
            Item votelistItem = votelistIds.get(id);
            if (votelistItem == null) {
                votelist.remove(id);
                votelistHasChanged = true;
            } else if (votelist.get(id) != null && votelistItem.getVote() != votelist.get(id).getVote()) {
                votelist.get(id).setVote(votelistItem.getVote());
                votelistHasChanged = true;
            }
        }
        boolean wishlistHasChanged = false;
        for (int id : new HashSet<>(wishlist.keySet())) {
            Item wishlistItem = wishlistIds.get(id);
            if (wishlistItem == null) {
                wishlist.remove(id);
                wishlistHasChanged = true;
            } else if (wishlist.get(id) != null && wishlistItem.getPriority() != wishlist.get(id).getPriority()) {
                wishlist.get(id).setPriority(wishlistItem.getPriority());
                wishlistHasChanged = true;
            }
        }

        Set<Integer> filteredMergedIds = new HashSet<>();
        for (Integer id : mergedIds) {
            Item vnlistItem = vnlistIds.get(id);
            Item votelistItem = votelistIds.get(id);
            Item wishlistItem = wishlistIds.get(id);

            /* 2 - Checking for VNs that have been added overtime */
            if (vnlistItem != null && vnlist.get(id) == null) {
                filteredMergedIds.add(id);
            }
            if (votelistItem != null && votelist.get(id) == null) {
                filteredMergedIds.add(id);
            }
            if (wishlistItem != null && wishlist.get(id) == null) {
                filteredMergedIds.add(id);
            }
        }

        if (!filteredMergedIds.isEmpty()) {
            /* VNs have been added or modified: updating the ids we're going to query */
            mergedIds.clear();
            mergedIds.addAll(filteredMergedIds);
            return true;
        } else {
            /* Updating persistent cache if VNs have been removed */
            if (vnlistHasChanged) {
                DB.saveVnlist(context);
            }
            if (votelistHasChanged) {
                DB.saveVotelist(context);
            }
            if (wishlistHasChanged) {
                DB.saveWishlist(context);
            }
            if (vnlistHasChanged || votelistHasChanged || wishlistHasChanged)
                shouldRefreshView = true;
        }

        return false;
    }

    public static boolean vnlistItemHasChanged(VNlistItem cachedItem, int newStatus, String newNotes) {
        String cachedNotes = cachedItem.getNotes();
        boolean itemHasChanged = newStatus != cachedItem.getStatus();
        itemHasChanged = itemHasChanged || cachedNotes == null && newNotes != null;
        itemHasChanged = itemHasChanged || cachedNotes != null && newNotes == null;
        itemHasChanged = itemHasChanged || cachedNotes != null && !cachedNotes.equals(newNotes);
        return itemHasChanged;
    }

    public static void openVNDetails(final Activity activity, final int vnId) {
        openVNDetails(activity, vnId, null, null);
    }

    public static void openVNDetails(final Activity activity, final int vnId, final Callback successCallback, final Callback errorCallback) {
        if (Cache.vns.get(vnId) != null) {
            Intent intent = new Intent(activity, VNDetailsActivity.class);
            intent.putExtra(VNTypeFragment.VN_ARG, vnId);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            if (successCallback != null) successCallback.call();
            return;
        }

        VNDBServer.get("vn", Cache.VN_FLAGS, "(id = " + vnId + ")", Options.create(false, 1), 0, activity, new Callback() {
            @Override
            protected void config() {
                if (!results.getItems().isEmpty()) {
                    Cache.vns.put(vnId, results.getItems().get(0));
                    Intent intent = new Intent(activity, VNDetailsActivity.class);
                    intent.putExtra(VNTypeFragment.VN_ARG, vnId);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    if (successCallback != null) successCallback.call();
                }
            }
        }, errorCallback != null ? errorCallback : Callback.errorCallback(activity));
    }

    /**
     * Getting the staff details from a VNDetailsActivity: we want the details of voiced (the voice actors of the character we're interested in).
     * To optimize the number of requests, we ask for all the staffs who voice any character of the VN. That's why we need more info than just our character's VA.
     *
     * @param activity
     * @param vnId       VN's id currently being viewed.
     * @param characters list of all the characters of the VN.
     * @param voiced     list of voice actors ID of our character (the character we just clicked, the one we want).
     */

    public static void getStaff(final Activity activity, final int vnId, List<Item> characters, List<CharacterVoiced> voiced, final Callback successCallback) {
        // 1 - Looking for the staff details in memory
        boolean shouldFetch = false;
        for (CharacterVoiced va : voiced) {
            if (Cache.staff.get(va.getId()) == null) {
                shouldFetch = true;
                break;
            }
        }

        if (!shouldFetch) {
            if (successCallback != null) successCallback.call();
            return;
        }

        // 2 - Looking for the staff details in the DB (for our character only)

        // TODO: look for the staff OF OUR CHARACTER ONLY in the DB before sending a request

        if (!shouldFetch) {
            if (successCallback != null) successCallback.call();
            return;
        }

        // 3 - Sending a request to get the staff details

        /* LinkedHashSet because we need a Set (staff ids present only once) which keeps the order (there MAY exceptionnally be more
        than one page of results, if it's the case we just fetch the first one but make sure that our staff id was in it) */
        Set<Integer> filteredVoiced = new LinkedHashSet<>();
        for (CharacterVoiced va : voiced) filteredVoiced.add(va.getId()); // Important: adding our staff IDs first
        for (Item character : characters) {
            for (CharacterVoiced va : character.getVoiced()) {
                if (va.getVid() == vnId) {
                    filteredVoiced.add(va.getId());
                }
            }
        }

        VNDBServer.get("staff", Cache.STAFF_FLAGS, "(id = [" + TextUtils.join(",", filteredVoiced) + "])", Options.create(false, 1), 0, activity, new Callback() {
            @Override
            protected void config() {
                if (!results.getItems().isEmpty()) {
                    for (Item staff : results.getItems()) {
                        Cache.staff.put(staff.getId(), staff);
                    }
                    // TODO: save ALL the staff in the DB in the request callback

                    if (successCallback != null) successCallback.call();
                }
            }
        }, Callback.errorCallback(activity));
    }

    public static void openStaff(final Activity activity, final int staffId) {
        /* At this point we are sure that the staff details are in memory (and even if something goes terribly wrong and it's
        somehow not the case, there is still a check that fetch everything we need in StaffActivity) */
        Intent intent = new Intent(activity, StaffActivity.class);
        intent.putExtra(StaffActivity.STAFF_ID, staffId);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public static void saveToCache(Context context, String filename, Object object) {
        File file = new File(context.getFilesDir(), filename);
        try {
            JSON.mapper.writeValue(file, object);
        } catch (IOException e) {
            Utils.processException(e);
        }
    }

    public static boolean loadFromCache(Context context) {
        if (loadedFromCache || SettingsManager.isEmptyAccount(context)) return true;

        vnlist = DB.loadVnlist(context);
        votelist = DB.loadVotelist(context);
        wishlist = DB.loadWishlist(context);
        vns.putAll(DB.loadVns(context));

        sortAll(context);
        loadedFromCache = vnlist.size() > 0 || votelist.size() > 0 || wishlist.size() > 0;

        return loadedFromCache;
    }

    public static void loadStatsFromCache(Context context) {
        File dbstatsFile = new File(context.getFilesDir(), DBSTATS_CACHE);
        if (dbstatsFile.exists()) {
            try {
                dbstats = JSON.mapper.readValue(dbstatsFile, DbStats.class);
            } catch (IOException e) {
                Utils.processException(e);
            }
        }
    }

    public static void clearCache(Context context) {
        DB.clear(context);
        vnlist = new LinkedHashMap<>();
        votelist = new LinkedHashMap<>();
        wishlist = new LinkedHashMap<>();
        vns = new LinkedHashMap<>();
        loadedFromCache = false;
    }

    public static void loadStats(final Context context, final Callback successCallback, Callback errorCallback, boolean forceRefresh) {
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
        }, errorCallback);
    }

    public static void sortAll(Context context) {
        sort(context, vnlist);
        sort(context, votelist);
        sort(context, wishlist);
    }

    public static <V extends CacheItem> void sort(final Context context, LinkedHashMap<Integer, V> list) {
        List<Map.Entry<Integer, V>> entries = new ArrayList<>(list.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Integer, V>>() {
            public int compare(Map.Entry<Integer, V> a, Map.Entry<Integer, V> b) {
                Map.Entry<Integer, V> first = a, second = b;
                if (SettingsManager.getReverseSort(context)) {
                    // Reverse sort : swapping a and b
                    first = b;
                    second = a;
                }
                Item firstValue;
                Item secondValue;

                switch (SettingsManager.getSort(context)) {
                    case 1:
                        firstValue = vns.get(first.getKey());
                        secondValue = vns.get(second.getKey());
                        if (firstValue == null && secondValue == null) return 0;
                        if (firstValue == null) return -1;
                        if (secondValue == null) return 1;
                        return firstValue.getTitle().compareTo(secondValue.getTitle());
                    case 2:
                        firstValue = vns.get(first.getKey());
                        secondValue = vns.get(second.getKey());
                        if (firstValue == null && secondValue == null) return 0;
                        if (firstValue == null) return -1;
                        if (secondValue == null) return 1;
                        String releasedA = firstValue.getReleased();
                        String releasedB = secondValue.getReleased();
                        if (releasedA == null && releasedB == null) return 0;
                        if (releasedA == null) return -1;
                        if (releasedB == null) return 1;
                        return releasedA.compareTo(releasedB);
                    case 3:
                        firstValue = vns.get(first.getKey());
                        secondValue = vns.get(second.getKey());
                        if (firstValue == null && secondValue == null) return 0;
                        if (firstValue == null) return -1;
                        if (secondValue == null) return 1;
                        return Integer.valueOf(firstValue.getLength()).compareTo(secondValue.getLength());
                    case 4:
                        firstValue = vns.get(first.getKey());
                        secondValue = vns.get(second.getKey());
                        if (firstValue == null && secondValue == null) return 0;
                        if (firstValue == null) return -1;
                        if (secondValue == null) return 1;
                        return Double.valueOf(firstValue.getPopularity()).compareTo(secondValue.getPopularity());
                    case 5:
                        firstValue = vns.get(first.getKey());
                        secondValue = vns.get(second.getKey());
                        if (firstValue == null && secondValue == null) return 0;
                        if (firstValue == null) return -1;
                        if (secondValue == null) return 1;
                        return Double.valueOf(firstValue.getRating()).compareTo(secondValue.getRating());
                    case 6:
                        VNlistItem vnlistA = Cache.vnlist.get(first.getKey());
                        VNlistItem vnlistB = Cache.vnlist.get(second.getKey());
                        if (vnlistA == null && vnlistB == null) return 0;
                        if (vnlistA == null) return -1;
                        if (vnlistB == null) return 1;
                        return Integer.valueOf(vnlistA.getStatus()).compareTo(vnlistB.getStatus());
                    case 7:
                        VotelistItem votelistA = Cache.votelist.get(first.getKey());
                        VotelistItem votelistB = Cache.votelist.get(second.getKey());
                        if (votelistA == null && votelistB == null) return 0;
                        if (votelistA == null) return -1;
                        if (votelistB == null) return 1;
                        return Integer.valueOf(votelistA.getVote()).compareTo(votelistB.getVote());
                    case 8:
                        WishlistItem wishlistA = Cache.wishlist.get(first.getKey());
                        WishlistItem wishlistB = Cache.wishlist.get(second.getKey());
                        if (wishlistA == null && wishlistB == null) return 0;
                        if (wishlistA == null) return -1;
                        if (wishlistB == null) return 1;
                        return Integer.valueOf(wishlistA.getPriority()).compareTo(wishlistB.getPriority());
                    default:
                        return first.getKey().compareTo(second.getKey());
                }
            }
        });
        list.clear();
        for (Map.Entry<Integer, V> entry : entries) {
            list.put(entry.getKey(), entry.getValue());
        }
    }

    public static int[] getStatusCount() {
        int[] res = new int[5];
        for (VNlistItem element : vnlist.values()) {
            res[element.getStatus()]++;
        }
        return res;
    }

    public static int[] getWishCount() {
        int[] res = new int[4];
        for (WishlistItem element : wishlist.values()) {
            res[element.getPriority()]++;
        }
        return res;
    }

    public static int[] getVoteCount() {
        int[] res = new int[5];
        for (VotelistItem element : votelist.values()) {
            if (element.getVote() >= 90) {
                res[0]++;
            } else if (element.getVote() >= 70) {
                res[1]++;
            } else if (element.getVote() >= 50) {
                res[2]++;
            } else if (element.getVote() >= 30) {
                res[3]++;
            } else {
                res[4]++;
            }
        }
        return res;
    }
}