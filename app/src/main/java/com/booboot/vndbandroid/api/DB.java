package com.booboot.vndbandroid.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.booboot.vndbandroid.model.vndb.Anime;
import com.booboot.vndbandroid.model.vndb.CharacterVoiced;
import com.booboot.vndbandroid.model.vndb.Item;
import com.booboot.vndbandroid.model.vndb.Links;
import com.booboot.vndbandroid.model.vndb.Media;
import com.booboot.vndbandroid.model.vndb.Producer;
import com.booboot.vndbandroid.model.vndb.Relation;
import com.booboot.vndbandroid.model.vndb.Screen;
import com.booboot.vndbandroid.model.vndb.VnStaff;
import com.booboot.vndbandroid.model.vndbandroid.VNlistItem;
import com.booboot.vndbandroid.model.vndbandroid.VotelistItem;
import com.booboot.vndbandroid.model.vndbandroid.WishlistItem;
import com.booboot.vndbandroid.model.vnstat.SimilarNovel;
import com.booboot.vndbandroid.util.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by od on 05/07/2016.
 */
public class DB extends SQLiteOpenHelper {
    private static DB instance;
    private final static String NAME = "VNDB_ANDROID";
    private final static int VERSION = 2;
    private final static int SQLITE_LIMIT_COMPOUND_SELECT = 500;

    private final static String TABLE_VNLIST = "vnlist";
    private final static String TABLE_VOTELIST = "votelist";
    private final static String TABLE_WISHLIST = "wishlist";
    private final static String TABLE_VN = "vn";
    private final static String TABLE_LANGUAGES = "languages";
    private final static String TABLE_ORIG_LANG = "orig_lang";
    private final static String TABLE_PLATFORMS = "platforms";
    private final static String TABLE_ANIME = "anime";
    private final static String TABLE_RELATION = "relation";
    private final static String TABLE_TAGS = "tags";
    private final static String TABLE_SCREENS = "screens";
    private final static String TABLE_VN_CHARACTER = "vn_character";
    private final static String TABLE_CHARACTER = "character";
    private final static String TABLE_CHARACTER_VOICED = "character_voiced";
    private final static String TABLE_VN_STAFF = "vn_staff";
    private final static String TABLE_STAFF = "staff";
    private final static String TABLE_TRAITS = "traits";
    private final static String TABLE_VN_RELEASE = "vn_release";
    private final static String TABLE_RELEASE = "release";
    private final static String TABLE_RELEASE_LANGUAGES = "release_languages";
    private final static String TABLE_RELEASE_PLATFORMS = "release_platforms";
    private final static String TABLE_RELEASE_MEDIA = "release_media";
    private final static String TABLE_RELEASE_PRODUCER = "release_producer";
    private final static String TABLE_PRODUCER = "producer";
    private final static String TABLE_SIMILAR_NOVELS = "similar_novels";
    private final static String TABLE_RECOMMENDATIONS = "recommendations";

    private DB(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_VNLIST + " (" +
                "vn INTEGER PRIMARY KEY, " +
                "added INTEGER, " +
                "status INTEGER, " +
                "notes TEXT " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_VOTELIST + " (" +
                "vn INTEGER PRIMARY KEY, " +
                "added INTEGER, " +
                "vote INTEGER " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_WISHLIST + " (" +
                "vn INTEGER PRIMARY KEY, " +
                "added INTEGER, " +
                "priority INTEGER " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_VN + " (" +
                "id INTEGER PRIMARY KEY, " +
                "title TEXT, " +
                "original TEXT, " +
                "released TEXT, " +
                "aliases TEXT, " +
                "length INTEGER, " +
                "description TEXT, " +
                "wikipedia TEXT, " +
                "encubed TEXT, " +
                "renai TEXT, " +
                "image TEXT, " +
                "image_nsfw INTEGER, " +
                "popularity REAL, " +
                "rating REAL, " +
                "votecount INTEGER " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_LANGUAGES + " (" +
                "vn INTEGER, " +
                "name TEXT, " +
                "PRIMARY KEY (vn, name)" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_ORIG_LANG + " (" +
                "vn INTEGER, " +
                "name TEXT, " +
                "PRIMARY KEY (vn, name)" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_PLATFORMS + " (" +
                "vn INTEGER, " +
                "name TEXT, " +
                "PRIMARY KEY (vn, name)" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_ANIME + " (" +
                "id INTEGER, " +
                "vn INTEGER, " +
                "ann_id INTEGER, " +
                "nfo_id INTEGER, " +
                "title_romaji TEXT, " +
                "title_kanji TEXT, " +
                "year INTEGER, " +
                "type TEXT, " +
                "PRIMARY KEY (id, vn)" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_RELATION + " (" +
                "id INTEGER, " +
                "vn INTEGER, " +
                "relation TEXT, " +
                "title TEXT, " +
                "original TEXT, " +
                "PRIMARY KEY (id, vn)" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_TAGS + " (" +
                "id INTEGER, " +
                "vn INTEGER, " +
                "score INTEGER, " +
                "spoiler_level INTEGER, " +
                "PRIMARY KEY (id, vn)" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_SCREENS + " (" +
                "vn INTEGER, " +
                "image TEXT, " +
                "rid INTEGER, " +
                "nsfw INTEGER, " +
                "height INTEGER, " +
                "width INTEGER, " +
                "PRIMARY KEY (vn, image)" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_VN_CHARACTER + " (" +
                "vn INTEGER, " +
                "character INTEGER, " +
                "PRIMARY KEY (vn, character)" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_CHARACTER + " (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "original TEXT, " +
                "gender TEXT, " +
                "bloodt TEXT, " +
                "birthday INTEGER, " +
                "birthmonth INTEGER, " +
                "aliases TEXT, " +
                "description TEXT, " +
                "image TEXT, " +
                "bust INTEGER, " +
                "waist INTEGER, " +
                "hip INTEGER, " +
                "height INTEGER, " +
                "weight INTEGER, " +
                "vns TEXT " + // serialized JSON
                ")");

        db.execSQL("CREATE TABLE " + TABLE_TRAITS + " (" +
                "character INTEGER, " +
                "trait INTEGER, " +
                "spoiler_level INTEGER, " +
                "PRIMARY KEY (character, trait) " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_VN_RELEASE + " (" +
                "vn INTEGER, " +
                "release TEXT, " +
                "PRIMARY KEY (vn, release) " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_RELEASE + " (" +
                "id INTEGER PRIMARY KEY, " +
                "title TEXT, " +
                "original TEXT, " +
                "released TEXT, " +
                "type TEXT, " +
                "patch INTEGER, " +
                "freeware INTEGER, " +
                "doujin INTEGER, " +
                "website TEXT, " +
                "notes TEXT, " +
                "minage INTEGER, " +
                "gtin TEXT, " +
                "catalog TEXT " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_RELEASE_LANGUAGES + " (" +
                "release INTEGER, " +
                "name TEXT, " +
                "PRIMARY KEY (release, name) " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_RELEASE_PLATFORMS + " (" +
                "release INTEGER, " +
                "name TEXT, " +
                "PRIMARY KEY (release, name) " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_RELEASE_MEDIA + " (" +
                "release INTEGER, " +
                "medium TEXT, " +
                "qty INTEGER, " +
                "PRIMARY KEY (release, medium) " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_RELEASE_PRODUCER + " (" +
                "release INTEGER, " +
                "producer INTEGER, " +
                "developer INTEGER, " +
                "publisher INTEGER, " +
                "PRIMARY KEY (release, producer) " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_PRODUCER + " (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "original TEXT, " +
                "type TEXT " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_SIMILAR_NOVELS + " (" +
                "id INTEGER, " +
                "vn INTEGER, " +
                "similarity REAL, " +
                "title TEXT, " +
                "image TEXT, " +
                "PRIMARY KEY (id, vn) " +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_RECOMMENDATIONS + " (" +
                "id INTEGER PRIMARY KEY, " +
                "similarity REAL, " +
                "title TEXT, " +
                "image TEXT, " +
                "released TEXT " +
                ")");

        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_LANGUAGES + "_vn ON " + TABLE_LANGUAGES + "(vn)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_PLATFORMS + "_vn ON " + TABLE_PLATFORMS + "(vn)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_ANIME + "_vn ON " + TABLE_ANIME + "(vn)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_RELATION + "_vn ON " + TABLE_RELATION + "(vn)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_TAGS + "_vn ON " + TABLE_TAGS + "(vn)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_SCREENS + "_vn ON " + TABLE_SCREENS + "(vn)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_VN_CHARACTER + "_vn ON " + TABLE_VN_CHARACTER + "(vn)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_TRAITS + "_character ON " + TABLE_TRAITS + "(character)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_VN_RELEASE + "_vn ON " + TABLE_VN_RELEASE + "(vn)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_RELEASE_LANGUAGES + "_release ON " + TABLE_RELEASE_LANGUAGES + "(release)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_RELEASE_PLATFORMS + "_release ON " + TABLE_RELEASE_PLATFORMS + "(release)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_RELEASE_MEDIA + "_release ON " + TABLE_RELEASE_MEDIA + "(release)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_RELEASE_PRODUCER + "_release ON " + TABLE_RELEASE_PRODUCER + "(release)");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_SIMILAR_NOVELS + "_vn ON " + TABLE_SIMILAR_NOVELS + "(vn)");

        onUpgrade(db, 1, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("D", "onUpgrade : " + oldVersion + " ; " + newVersion);
        /* For each new upgrade, clear the database so it can be recreated properly */
        clear(db);

        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE " + TABLE_VN_STAFF + " (" +
                    "vid INTEGER, " +
                    "sid INTEGER, " +
                    "aid INTEGER, " +
                    "name TEXT, " +
                    "original TEXT, " +
                    "role TEXT, " +
                    "note TEXT" +
                    ")");

            db.execSQL("CREATE TABLE " + TABLE_CHARACTER_VOICED + " (" +
                    "id INTEGER, " +
                    "cid INTEGER, " +
                    "vid INTEGER, " +
                    "aid INTEGER, " +
                    "note TEXT, " +
                    "PRIMARY KEY (id, cid, vid) " +
                    ")");

            db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_VN_STAFF + "_vn ON " + TABLE_VN_STAFF + "(vid)");
            db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_CHARACTER_VOICED + "_id ON " + TABLE_CHARACTER_VOICED + "(id)");
            db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_CHARACTER_VOICED + "_cid ON " + TABLE_CHARACTER_VOICED + "(cid)");
            db.execSQL("CREATE INDEX IF NOT EXISTS " + TABLE_CHARACTER_VOICED + "_vid ON " + TABLE_CHARACTER_VOICED + "(vid)");
        }

//        if (oldVersion < 3) {
//        }
    }

    public static void saveVnlist(Context context) {
        saveVnlist(context, true, true);
    }

    public static void saveVnlist(Context context, boolean beginTransaction, boolean endTransaction) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        StringBuilder query = new StringBuilder("INSERT INTO ").append(TABLE_VNLIST).append(" VALUES ");
        int itemsToInsert = 0;
        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        LinkedHashMap<Integer, VNlistItem> alreadyInsertedItems = loadVnlist(context);
        if (beginTransaction)
            db.beginTransaction();

        for (int vn : Cache.vnlist.keySet()) {
            VNlistItem item = Cache.vnlist.get(vn);

            if (alreadyInsertedItems.get(vn) != null) {
                VNlistItem existingItem = alreadyInsertedItems.get(vn);

                if (Cache.vnlistItemHasChanged(existingItem, item.getStatus(), item.getNotes())) {
                    ContentValues values = new ContentValues();
                    values.put("status", item.getStatus());
                    values.put("notes", item.getNotes());
                    db.update(TABLE_VNLIST, values, "vn=?", new String[]{item.getVn() + ""});
                }
                continue;
            }

            query.append("(")
                    .append(item.getVn()).append(",")
                    .append(item.getAdded()).append(",")
                    .append(item.getStatus()).append(",")
                    .append(formatString(item.getNotes()))
                    .append("),");
            itemsToInsert = checkInsertLimit(db, query, itemsToInsert, TABLE_VNLIST);
        }

        if (itemsToInsert > 0) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }

        if (endTransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    private static int checkInsertLimit(SQLiteDatabase db, StringBuilder query, int itemsToInsert, String tableName) {
        itemsToInsert++;
        if (itemsToInsert >= SQLITE_LIMIT_COMPOUND_SELECT) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
            query.delete(0, query.length());
            query.append("INSERT INTO ").append(tableName).append(" VALUES ");
            itemsToInsert = 0;
        }
        return itemsToInsert;
    }

    public static void saveVotelist(Context context) {
        saveVotelist(context, true, true);
    }

    public static void saveVotelist(Context context, boolean beginTransaction, boolean endTransaction) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        StringBuilder query = new StringBuilder("INSERT INTO ").append(TABLE_VOTELIST).append(" VALUES ");
        int itemsToInsert = 0;
        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        LinkedHashMap<Integer, VotelistItem> alreadyInsertedItems = loadVotelist(context);
        if (beginTransaction)
            db.beginTransaction();

        for (int vn : Cache.votelist.keySet()) {
            VotelistItem item = Cache.votelist.get(vn);

            if (alreadyInsertedItems.get(vn) != null) {
                VotelistItem existingItem = alreadyInsertedItems.get(vn);

                if (existingItem.getVote() != item.getVote()) {
                    ContentValues values = new ContentValues();
                    values.put("vote", item.getVote());
                    db.update(TABLE_VOTELIST, values, "vn=?", new String[]{item.getVn() + ""});
                }
                continue;
            }

            query.append("(")
                    .append(item.getVn()).append(",")
                    .append(item.getAdded()).append(",")
                    .append(item.getVote())
                    .append("),");
            itemsToInsert = checkInsertLimit(db, query, itemsToInsert, TABLE_VOTELIST);
        }

        if (itemsToInsert > 0) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }

        if (endTransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public static void saveWishlist(Context context) {
        saveWishlist(context, true, true);
    }

    public static void saveWishlist(Context context, boolean beginTransaction, boolean endTransaction) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        StringBuilder query = new StringBuilder("INSERT INTO ").append(TABLE_WISHLIST).append(" VALUES ");
        int itemsToInsert = 0;
        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        LinkedHashMap<Integer, WishlistItem> alreadyInsertedItems = loadWishlist(context);
        if (beginTransaction)
            db.beginTransaction();

        for (int vn : Cache.wishlist.keySet()) {
            WishlistItem item = Cache.wishlist.get(vn);

            if (alreadyInsertedItems.get(vn) != null) {
                WishlistItem existingItem = alreadyInsertedItems.get(vn);

                if (existingItem.getPriority() != item.getPriority()) {
                    ContentValues values = new ContentValues();
                    values.put("priority", item.getPriority());
                    db.update(TABLE_WISHLIST, values, "vn=?", new String[]{item.getVn() + ""});
                }
                continue;
            }

            query.append("(")
                    .append(item.getVn()).append(",")
                    .append(item.getAdded()).append(",")
                    .append(item.getPriority())
                    .append("),");
            itemsToInsert = checkInsertLimit(db, query, itemsToInsert, TABLE_WISHLIST);
        }

        if (itemsToInsert > 0) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }

        if (endTransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public static void deleteVnlist(Context context, int vnId) {
        deleteVnlist(context, vnId, true, true);
    }

    public static void deleteVnlist(Context context, int vnId, boolean beginTransaction, boolean endTransaction) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        if (beginTransaction) db.beginTransaction();

        db.execSQL("DELETE FROM " + TABLE_VNLIST + " WHERE vn = " + vnId);

        if (endTransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public static void deleteVotelist(Context context, int vnId) {
        deleteVotelist(context, vnId, true, true);
    }

    public static void deleteVotelist(Context context, int vnId, boolean beginTransaction, boolean endTransaction) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        if (beginTransaction) db.beginTransaction();

        db.execSQL("DELETE FROM " + TABLE_VOTELIST + " WHERE vn = " + vnId);

        if (endTransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public static void deleteWishlist(Context context, int vnId) {
        deleteWishlist(context, vnId, true, true);
    }

    public static void deleteWishlist(Context context, int vnId, boolean beginTransaction, boolean endTransaction) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        if (beginTransaction) db.beginTransaction();

        db.execSQL("DELETE FROM " + TABLE_WISHLIST + " WHERE vn = " + vnId);

        if (endTransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public static void deleteVN(Context context, int vnId, boolean beginTransaction, boolean endTransaction) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        if (beginTransaction) db.beginTransaction();

        db.execSQL("DELETE FROM " + TABLE_VN + " WHERE id = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_LANGUAGES + " WHERE vn = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_ORIG_LANG + " WHERE vn = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_PLATFORMS + " WHERE vn = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_ANIME + " WHERE vn = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_RELATION + " WHERE vn = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_TAGS + " WHERE vn = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_SCREENS + " WHERE vn = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_VN_CHARACTER + " WHERE vn = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_VN_RELEASE + " WHERE vn = " + vnId);
        db.execSQL("DELETE FROM " + TABLE_SIMILAR_NOVELS + " WHERE vn = " + vnId);

        if (endTransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public static void saveVNs(Context context) {
        saveVNs(context, true, true);
    }

    public static void saveVNs(Context context, boolean beginTransaction, boolean endTransaction) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        int itemsToInsert[] = new int[9];
        StringBuilder[] queries = new StringBuilder[9];
        queries[0] = new StringBuilder("INSERT INTO ").append(TABLE_VN).append(" VALUES ");
        queries[1] = new StringBuilder("INSERT INTO ").append(TABLE_LANGUAGES).append(" VALUES ");
        queries[2] = new StringBuilder("INSERT INTO ").append(TABLE_ORIG_LANG).append(" VALUES ");
        queries[3] = new StringBuilder("INSERT INTO ").append(TABLE_PLATFORMS).append(" VALUES ");
        queries[4] = new StringBuilder("INSERT INTO ").append(TABLE_ANIME).append(" VALUES ");
        queries[5] = new StringBuilder("INSERT INTO ").append(TABLE_RELATION).append(" VALUES ");
        queries[6] = new StringBuilder("INSERT INTO ").append(TABLE_TAGS).append(" VALUES ");
        queries[7] = new StringBuilder("INSERT INTO ").append(TABLE_SCREENS).append(" VALUES ");
        queries[8] = new StringBuilder("INSERT INTO ").append(TABLE_VN_STAFF).append(" VALUES ");

        if (beginTransaction)
            db.beginTransaction();

        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        Cursor cursor = db.rawQuery("select id from " + TABLE_VN, new String[]{});
        Map<Integer, Item> alreadyInsertedItems = new HashMap<>();
        while (cursor.moveToNext()) {
            alreadyInsertedItems.put(cursor.getInt(0), new Item(cursor.getInt(0)));
        }
        cursor.close();

        for (int vn : Cache.vns.keySet()) {
            /* Do not insert if already inserted OR not present in any of the lists (not very useful to fill the database with VNs that are not even in any list) */
            if (alreadyInsertedItems.get(vn) != null || Cache.vnlist.get(vn) == null && Cache.votelist.get(vn) == null && Cache.wishlist.get(vn) == null) {
                continue;
            }

            Item item = Cache.vns.get(vn);

            if (item.getLanguages() == null || item.getOrig_lang() == null || item.getPlatforms() == null || item.getAnime() == null || item.getRelations() == null || item.getScreens() == null || item.getTags() == null || item.getStaff() == null) {
                /* A list attribute of the VN is null: that shouldn't be possible because the API NEVER returns null for these attributes (at least []).
                If that ever happens, we don't insert this VN into the database because it would corrupt it with incomplete data. */
                continue;
            }

            queries[0].append("(")
                    .append(item.getId()).append(",")
                    .append(formatString(item.getTitle())).append(",")
                    .append(formatString(item.getOriginal())).append(",")
                    .append(formatString(item.getReleased())).append(",")
                    .append(formatString(item.getAliases())).append(",")
                    .append(item.getLength()).append(",")
                    .append(formatString(item.getDescription())).append(",")
                    .append(formatString(item.getLinks().getWikipedia())).append(",")
                    .append(formatString(item.getLinks().getEncubed())).append(",")
                    .append(formatString(item.getLinks().getRenai())).append(",")
                    .append(formatString(item.getImage())).append(",")
                    .append(formatBool(item.isImage_nsfw())).append(",")
                    .append(item.getPopularity()).append(",")
                    .append(item.getRating()).append(",")
                    .append(item.getVotecount())
                    .append("),");
            itemsToInsert[0] = checkInsertLimit(db, queries[0], itemsToInsert[0], TABLE_VN);

            for (String language : item.getLanguages()) {
                queries[1].append("(")
                        .append(item.getId()).append(",")
                        .append(formatString(language))
                        .append("),");
                itemsToInsert[1] = checkInsertLimit(db, queries[1], itemsToInsert[1], TABLE_LANGUAGES);
            }

            for (String origLang : item.getOrig_lang()) {
                queries[2].append("(")
                        .append(item.getId()).append(",")
                        .append(formatString(origLang))
                        .append("),");
                itemsToInsert[2] = checkInsertLimit(db, queries[2], itemsToInsert[2], TABLE_ORIG_LANG);
            }

            for (String platform : item.getPlatforms()) {
                queries[3].append("(")
                        .append(item.getId()).append(",")
                        .append(formatString(platform))
                        .append("),");
                itemsToInsert[3] = checkInsertLimit(db, queries[3], itemsToInsert[3], TABLE_PLATFORMS);
            }

            for (Anime anime : item.getAnime()) {
                queries[4].append("(")
                        .append(anime.getId()).append(",")
                        .append(item.getId()).append(",")
                        .append(anime.getAnn_id()).append(",")
                        .append(formatString(anime.getNfo_id())).append(",")
                        .append(formatString(anime.getTitle_romaji())).append(",")
                        .append(formatString(anime.getTitle_kanji())).append(",")
                        .append(anime.getYear()).append(",")
                        .append(formatString(anime.getType()))
                        .append("),");
                itemsToInsert[4] = checkInsertLimit(db, queries[4], itemsToInsert[4], TABLE_ANIME);
            }

            for (Relation relation : item.getRelations()) {
                queries[5].append("(")
                        .append(relation.getId()).append(",")
                        .append(item.getId()).append(",")
                        .append(formatString(relation.getRelation())).append(",")
                        .append(formatString(relation.getTitle())).append(",")
                        .append(formatString(relation.getOriginal()))
                        .append("),");
                itemsToInsert[5] = checkInsertLimit(db, queries[5], itemsToInsert[5], TABLE_RELATION);
            }

            for (List<Number> tagInfo : item.getTags()) {
                queries[6].append("(")
                        .append(tagInfo.get(0)).append(",")
                        .append(item.getId()).append(",")
                        .append(tagInfo.get(1)).append(",")
                        .append(tagInfo.get(2))
                        .append("),");
                itemsToInsert[6] = checkInsertLimit(db, queries[6], itemsToInsert[6], TABLE_TAGS);
            }

            for (Screen screen : item.getScreens()) {
                queries[7].append("(")
                        .append(item.getId()).append(",")
                        .append(formatString(screen.getImage())).append(",")
                        .append(screen.getRid()).append(",")
                        .append(formatBool(screen.isNsfw())).append(",")
                        .append(screen.getHeight()).append(",")
                        .append(screen.getWidth())
                        .append("),");
                itemsToInsert[7] = checkInsertLimit(db, queries[7], itemsToInsert[7], TABLE_SCREENS);
            }

            for (VnStaff staff : item.getStaff()) {
                queries[8].append("(")
                        .append(item.getId()).append(",")
                        .append(staff.getSid()).append(",")
                        .append(staff.getAid()).append(",")
                        .append(formatString(staff.getName())).append(",")
                        .append(formatString(staff.getOriginal())).append(",")
                        .append(formatString(staff.getRole())).append(",")
                        .append(formatString(staff.getNote()))
                        .append("),");
                itemsToInsert[8] = checkInsertLimit(db, queries[8], itemsToInsert[8], TABLE_VN_STAFF);
            }
        }

        exec(db, queries, itemsToInsert);

        if (endTransaction) {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public static void saveCharacters(Context context, List<Item> characters, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        StringBuilder[] queries = new StringBuilder[4];
        queries[0] = new StringBuilder("INSERT INTO ").append(TABLE_VN_CHARACTER).append(" VALUES ");
        queries[1] = new StringBuilder("INSERT INTO ").append(TABLE_CHARACTER).append(" VALUES ");
        queries[2] = new StringBuilder("INSERT INTO ").append(TABLE_TRAITS).append(" VALUES ");
        queries[3] = new StringBuilder("INSERT INTO ").append(TABLE_CHARACTER_VOICED).append(" VALUES ");
        int[] itemsToInsert = new int[4];

        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        LinkedHashMap<Integer, Boolean> alreadyInsertedCharacters = new LinkedHashMap<>();
        LinkedHashMap<Integer, Boolean> alreadyLinkedCharacters = new LinkedHashMap<>();
        LinkedHashMap<Integer, Boolean> newlyInsertedCharacters = new LinkedHashMap<>();
        Set<Integer> characterIds = new HashSet<>();
        for (Item character : characters) {
            characterIds.add(character.getId());
        }

        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_CHARACTER + " WHERE id IN (" + TextUtils.join(",", characterIds) + ")", new String[]{});
        while (cursor.moveToNext()) {
            alreadyInsertedCharacters.put(cursor.getInt(0), true);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT character FROM " + TABLE_VN_CHARACTER + " WHERE vn = " + vnId, new String[]{});
        while (cursor.moveToNext()) {
            alreadyLinkedCharacters.put(cursor.getInt(0), true);
        }
        cursor.close();

        db.beginTransaction();

        for (Item character : characters) {
            /* We've just inserted this character now so it is up to date : go to next */
            if (newlyInsertedCharacters.get(character.getId()) != null) continue;

            if (alreadyInsertedCharacters.get(character.getId()) != null) {
                /* The character's was already present BEFORE this method: deleting all for update */
                db.delete(TABLE_CHARACTER, "id=?", new String[]{character.getId() + ""});
                db.delete(TABLE_TRAITS, "character=?", new String[]{character.getId() + ""});
                db.delete(TABLE_CHARACTER_VOICED, "cid=?", new String[]{character.getId() + ""});
            }

            String vns = null;
            try {
                vns = JSON.mapper.writeValueAsString(character.getVns());
            } catch (JsonProcessingException e) {
            }
            queries[1].append("(")
                    .append(character.getId()).append(",")
                    .append(formatString(character.getName())).append(",")
                    .append(formatString(character.getOriginal())).append(",")
                    .append(formatString(character.getGender())).append(",")
                    .append(formatString(character.getBloodt())).append(",")
                    .append(character.getBirthday()[0]).append(",")
                    .append(character.getBirthday()[1]).append(",")
                    .append(formatString(character.getAliases())).append(",")
                    .append(formatString(character.getDescription())).append(",")
                    .append(formatString(character.getImage())).append(",")
                    .append(character.getBust()).append(",")
                    .append(character.getWaist()).append(",")
                    .append(character.getHip()).append(",")
                    .append(character.getHeight()).append(",")
                    .append(character.getWeight()).append(",")
                    .append(formatString(vns))
                    .append("),");
            itemsToInsert[1] = checkInsertLimit(db, queries[1], itemsToInsert[1], TABLE_CHARACTER);
            newlyInsertedCharacters.put(character.getId(), true);

            for (int[] trait : character.getTraits()) {
                queries[2].append("(")
                        .append(character.getId()).append(",")
                        .append(trait[0]).append(",")
                        .append(trait[1])
                        .append("),");
                itemsToInsert[2] = checkInsertLimit(db, queries[2], itemsToInsert[2], TABLE_TRAITS);
            }

            for (CharacterVoiced voiced : character.getVoiced()) {
                queries[3].append("(")
                        .append(voiced.getId()).append(",")
                        .append(character.getId()).append(",")
                        .append(voiced.getVid()).append(",")
                        .append(voiced.getAid()).append(",")
                        .append(formatString(voiced.getNote()))
                        .append("),");
                itemsToInsert[3] = checkInsertLimit(db, queries[3], itemsToInsert[3], TABLE_CHARACTER_VOICED);
            }

            if (alreadyLinkedCharacters.get(character.getId()) == null) {
                queries[0].append("(")
                        .append(vnId).append(",")
                        .append(character.getId())
                        .append("),");
                itemsToInsert[0] = checkInsertLimit(db, queries[0], itemsToInsert[0], TABLE_VN_CHARACTER);
                alreadyLinkedCharacters.put(character.getId(), true);
            }
        }

        exec(db, queries, itemsToInsert);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static List<Item> loadCharacters(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        LinkedHashMap<Integer, Item> res = new LinkedHashMap<>(); // vn -> character

        Cursor[] cursor = new Cursor[3];
        cursor[0] = db.rawQuery("SELECT c.* FROM  " + TABLE_VN_CHARACTER + " vnc INNER JOIN " + TABLE_CHARACTER + " c ON vnc.character = c.id WHERE vnc.vn = " + vnId, new String[]{});

        while (cursor[0].moveToNext()) {
            Item character = new Item(cursor[0].getInt(0));
            character.setName(cursor[0].getString(1));
            character.setOriginal(cursor[0].getString(2));
            character.setGender(cursor[0].getString(3));
            character.setBloodt(cursor[0].getString(4));
            character.setBirthday(new int[]{cursor[0].getInt(5), cursor[0].getInt(6)});
            character.setAliases(cursor[0].getString(7));
            character.setDescription(cursor[0].getString(8));
            character.setImage(cursor[0].getString(9));
            character.setBust(cursor[0].getInt(10));
            character.setWaist(cursor[0].getInt(11));
            character.setHip(cursor[0].getInt(12));
            character.setHeight(cursor[0].getInt(13));
            character.setWeight(cursor[0].getInt(14));
            try {
                //noinspection unchecked
                character.setVns((List<Object[]>) JSON.mapper.readValue(cursor[0].getString(15), new TypeReference<List<Object[]>>() {
                }));
            } catch (IOException e) {
                character.setVns(null);
            }

            res.put(cursor[0].getInt(0), character);
        }

        cursor[1] = db.rawQuery("SELECT * FROM " + TABLE_TRAITS + " WHERE character IN (" + TextUtils.join(",", res.keySet()) + ")", new String[]{});
        while (cursor[1].moveToNext()) {
            Item character = res.get(cursor[1].getInt(0));
            if (character == null) continue;
            if (character.getTraits() == null)
                character.setTraits(new ArrayList<int[]>());
            character.getTraits().add(new int[]{cursor[1].getInt(1), cursor[1].getInt(2)});
        }

        cursor[2] = db.rawQuery("SELECT * FROM " + TABLE_CHARACTER_VOICED + " WHERE cid IN (" + TextUtils.join(",", res.keySet()) + ")", new String[]{});
        while (cursor[2].moveToNext()) {
            Item character = res.get(cursor[2].getInt(1));
            if (character == null) continue;
            CharacterVoiced voiced = new CharacterVoiced();
            voiced.setId(cursor[2].getInt(0));
            voiced.setVid(cursor[2].getInt(2));
            voiced.setAid(cursor[2].getInt(3));
            voiced.setNote(cursor[2].getString(4));
            character.getVoiced().add(voiced);
        }

        for (Cursor c : cursor) c.close();

        return new ArrayList<>(res.values());
    }

    public static void saveReleases(Context context, List<Item> releases, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        StringBuilder[] queries = new StringBuilder[7];
        queries[0] = new StringBuilder("INSERT INTO ").append(TABLE_VN_RELEASE).append(" VALUES ");
        queries[1] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE).append(" VALUES ");
        queries[2] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE_LANGUAGES).append(" VALUES ");
        queries[3] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE_PLATFORMS).append(" VALUES ");
        queries[4] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE_MEDIA).append(" VALUES ");
        queries[5] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE_PRODUCER).append(" VALUES ");
        queries[6] = new StringBuilder("INSERT INTO ").append(TABLE_PRODUCER).append(" VALUES ");
        int[] itemsToInsert = new int[7];

        /* Retrieving all items to check if we have to INSERT or UPDATE */
        LinkedHashMap<Integer, Boolean> alreadyInsertedReleases = new LinkedHashMap<>();
        LinkedHashMap<Integer, Boolean> alreadyInsertedProducers = new LinkedHashMap<>();
        LinkedHashMap<Integer, Boolean> alreadyLinkedReleases = new LinkedHashMap<>();
        LinkedHashMap<Integer, Boolean> newlyInsertedProducers = new LinkedHashMap<>();
        LinkedHashMap<Integer, Boolean> newlyInsertedReleases = new LinkedHashMap<>();
        Set<Integer> releasesIds = new HashSet<>();
        Set<Integer> producersIds = new HashSet<>();
        for (Item release : releases) {
            releasesIds.add(release.getId());
            for (Producer producer : release.getProducers()) {
                producersIds.add(producer.getId());
            }
        }

        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_RELEASE + " WHERE id IN (" + TextUtils.join(",", releasesIds) + ")", new String[]{});
        while (cursor.moveToNext()) {
            alreadyInsertedReleases.put(cursor.getInt(0), true);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT id FROM " + TABLE_PRODUCER + " WHERE id IN (" + TextUtils.join(",", producersIds) + ")", new String[]{});
        while (cursor.moveToNext()) {
            alreadyInsertedProducers.put(cursor.getInt(0), true);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT release FROM " + TABLE_VN_RELEASE + " WHERE vn = " + vnId, new String[]{});
        while (cursor.moveToNext()) {
            alreadyLinkedReleases.put(cursor.getInt(0), true);
        }
        cursor.close();
        db.beginTransaction();

        for (Item release : releases) {
            /* We've just inserted this release now so it is up to date : go to next */
            if (newlyInsertedReleases.get(release.getId()) != null) continue;

            if (alreadyInsertedReleases.get(release.getId()) != null) {
                db.delete(TABLE_RELEASE, "id=?", new String[]{release.getId() + ""});
                db.delete(TABLE_RELEASE_LANGUAGES, "release=?", new String[]{release.getId() + ""});
                db.delete(TABLE_RELEASE_PLATFORMS, "release=?", new String[]{release.getId() + ""});
                db.delete(TABLE_RELEASE_MEDIA, "release=?", new String[]{release.getId() + ""});
                db.delete(TABLE_RELEASE_PRODUCER, "release=?", new String[]{release.getId() + ""});
            }

            queries[1].append("(")
                    .append(release.getId()).append(",")
                    .append(formatString(release.getTitle())).append(",")
                    .append(formatString(release.getOriginal())).append(",")
                    .append(formatString(release.getReleased())).append(",")
                    .append(formatString(release.getType())).append(",")
                    .append(formatBool(release.isPatch())).append(",")
                    .append(formatBool(release.isFreeware())).append(",")
                    .append(formatBool(release.isDoujin())).append(",")
                    .append(formatString(release.getWebsite())).append(",")
                    .append(formatString(release.getNotes())).append(",")
                    .append(release.getMinage()).append(",")
                    .append(formatString(release.getGtin())).append(",")
                    .append(formatString(release.getCatalog()))
                    .append("),");
            itemsToInsert[1] = checkInsertLimit(db, queries[1], itemsToInsert[1], TABLE_RELEASE);
            newlyInsertedReleases.put(release.getId(), true);

            if (release.getLanguages() != null) {
                for (String language : release.getLanguages()) {
                    queries[2].append("(")
                            .append(release.getId()).append(",")
                            .append(formatString(language))
                            .append("),");
                    itemsToInsert[2] = checkInsertLimit(db, queries[2], itemsToInsert[2], TABLE_RELEASE_LANGUAGES);
                }
            }

            if (release.getPlatforms() != null) {
                for (String platform : release.getPlatforms()) {
                    queries[3].append("(")
                            .append(release.getId()).append(",")
                            .append(formatString(platform))
                            .append("),");
                    itemsToInsert[3] = checkInsertLimit(db, queries[3], itemsToInsert[3], TABLE_RELEASE_PLATFORMS);
                }
            }

            if (release.getMedia() != null) {
                for (Media media : release.getMedia()) {
                    queries[4].append("(")
                            .append(release.getId()).append(",")
                            .append(formatString(media.getMedium())).append(",")
                            .append(media.getQty())
                            .append("),");
                    itemsToInsert[4] = checkInsertLimit(db, queries[4], itemsToInsert[4], TABLE_RELEASE_MEDIA);
                }
            }

            for (Producer producer : release.getProducers()) {
                queries[5].append("(")
                        .append(release.getId()).append(",")
                        .append(producer.getId()).append(",")
                        .append(formatBool(producer.isDeveloper())).append(",")
                        .append(formatBool(producer.isPublisher()))
                        .append("),");
                itemsToInsert[5] = checkInsertLimit(db, queries[5], itemsToInsert[5], TABLE_RELEASE_PRODUCER);

                /* We've just inserted this producer now so it is up to date : go to next */
                if (newlyInsertedProducers.get(producer.getId()) != null) continue;

                if (alreadyInsertedProducers.get(producer.getId()) != null) {
                    /* The producer already existed : removing it so it is updated (even if used in another VN) */
                    db.delete(TABLE_PRODUCER, "id=?", new String[]{producer.getId() + ""});
                }

                queries[6].append("(")
                        .append(producer.getId()).append(",")
                        .append(formatString(producer.getName())).append(",")
                        .append(formatString(producer.getOriginal())).append(",")
                        .append(formatString(producer.getType()))
                        .append("),");
                itemsToInsert[6] = checkInsertLimit(db, queries[6], itemsToInsert[6], TABLE_PRODUCER);
                newlyInsertedProducers.put(producer.getId(), true);
            }

            if (alreadyLinkedReleases.get(release.getId()) == null) {
                queries[0].append("(")
                        .append(vnId).append(",")
                        .append(release.getId())
                        .append("),");
                itemsToInsert[0] = checkInsertLimit(db, queries[0], itemsToInsert[0], TABLE_VN_RELEASE);
                alreadyLinkedReleases.put(release.getId(), true);
            }
        }

        exec(db, queries, itemsToInsert);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static List<Item> loadReleases(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        LinkedHashMap<Integer, Item> res = new LinkedHashMap<>(); // release_id -> release

        Cursor[] cursor = new Cursor[5];

        cursor[0] = db.rawQuery("SELECT r.* FROM  " + TABLE_VN_RELEASE + " vnr INNER JOIN " + TABLE_RELEASE + " r ON vnr.release = r.id WHERE vnr.vn = " + vnId, new String[]{});
        while (cursor[0].moveToNext()) {
            Item release = new Item(cursor[0].getInt(0));
            release.setTitle(cursor[0].getString(1));
            release.setOriginal(cursor[0].getString(2));
            release.setReleased(cursor[0].getString(3));
            release.setType(cursor[0].getString(4));
            release.setPatch(cursor[0].getInt(5) == 1);
            release.setFreeware(cursor[0].getInt(6) == 1);
            release.setDoujin(cursor[0].getInt(7) == 1);
            release.setWebsite(cursor[0].getString(8));
            release.setNotes(cursor[0].getString(9));
            release.setMinage(cursor[0].getInt(10));
            release.setGtin(cursor[0].getString(11));
            release.setCatalog(cursor[0].getString(12));
            release.setLanguages(new ArrayList<String>());
            release.setPlatforms(new ArrayList<String>());
            release.setMedia(new ArrayList<Media>());
            release.setProducers(new ArrayList<Producer>());

            res.put(cursor[0].getInt(0), release);
        }

        String releasesIds = TextUtils.join(",", res.keySet());
        cursor[1] = db.rawQuery("SELECT * FROM " + TABLE_RELEASE_LANGUAGES + " WHERE release IN (" + releasesIds + ")", new String[]{});
        while (cursor[1].moveToNext()) {
            Item release = res.get(cursor[1].getInt(0));
            if (release == null) continue;
            release.getLanguages().add(cursor[1].getString(1));
        }

        cursor[2] = db.rawQuery("SELECT * FROM " + TABLE_RELEASE_PLATFORMS + " WHERE release IN (" + releasesIds + ")", new String[]{});
        while (cursor[2].moveToNext()) {
            Item release = res.get(cursor[2].getInt(0));
            if (release == null) continue;
            release.getPlatforms().add(cursor[2].getString(1));
        }

        cursor[3] = db.rawQuery("SELECT * FROM " + TABLE_RELEASE_MEDIA + " WHERE release IN (" + releasesIds + ")", new String[]{});
        while (cursor[3].moveToNext()) {
            Item release = res.get(cursor[3].getInt(0));
            if (release == null) continue;
            release.getMedia().add(new Media(cursor[3].getString(1), cursor[3].getInt(2)));
        }

        cursor[4] = db.rawQuery("SELECT * FROM  " + TABLE_RELEASE_PRODUCER + " rp INNER JOIN " + TABLE_PRODUCER + " p ON rp.producer = p.id WHERE rp.release IN (" + releasesIds + ")", new String[]{});
        while (cursor[4].moveToNext()) {
            Item release = res.get(cursor[4].getInt(0));
            if (release == null) continue;

            /* Since we've made a join between 2 tables, it's quite hard to determine the column indexes for what we want, and SQLite may change
            it depending on the devices. So to be sure and avoid bugs, we manually check the column names */
            int id_column_index = 4, developer_column_index = 2, publisher_column_index = 3, name_column_index = 5, original_column_index = 6, type_column_index = 7;
            for (int i = 0; i < cursor[4].getColumnCount(); i++) {
                switch (cursor[4].getColumnName(i)) {
                    case "id":
                    case "release":
                        id_column_index = i;
                        break;
                    case "developer":
                        developer_column_index = i;
                        break;
                    case "publisher":
                        publisher_column_index = i;
                        break;
                    case "name":
                        name_column_index = i;
                        break;
                    case "original":
                        original_column_index = i;
                        break;
                    case "type":
                        type_column_index = i;
                        break;
                }
            }

            Producer producer = new Producer();
            producer.setId(cursor[4].getInt(id_column_index));
            producer.setDeveloper(cursor[4].getInt(developer_column_index) == 1);
            producer.setPublisher(cursor[4].getInt(publisher_column_index) == 1);
            producer.setName(cursor[4].getString(name_column_index));
            producer.setOriginal(cursor[4].getString(original_column_index));
            producer.setType(cursor[4].getString(type_column_index));
            release.getProducers().add(producer);
        }

        for (Cursor c : cursor) c.close();

        return new ArrayList<>(res.values());
    }

    public static void saveSimilarNovels(Context context, List<SimilarNovel> similarNovels, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        StringBuilder query = new StringBuilder("INSERT INTO ").append(TABLE_SIMILAR_NOVELS).append(" VALUES ");
        boolean somethingToInsert = false;

        LinkedHashMap<Integer, Boolean> alreadyLinkedNovels = new LinkedHashMap<>();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_SIMILAR_NOVELS + " WHERE vn = " + vnId, new String[]{});
        while (cursor.moveToNext()) {
            alreadyLinkedNovels.put(cursor.getInt(0), true);
        }
        cursor.close();

        db.beginTransaction();

        for (SimilarNovel similarNovel : similarNovels) {
            if (alreadyLinkedNovels.get(similarNovel.getNovelId()) == null) {
                query.append("(")
                        .append(similarNovel.getNovelId()).append(",")
                        .append(vnId).append(",")
                        .append(similarNovel.getSimilarity()).append(",")
                        .append(formatString(similarNovel.getTitle())).append(",")
                        .append(formatString(similarNovel.getImage()))
                        .append("),");
                somethingToInsert = true;
                alreadyLinkedNovels.put(similarNovel.getNovelId(), true);
            }
        }

        if (somethingToInsert) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void saveRecommendations(Context context, List<SimilarNovel> recommendations) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        StringBuilder query = new StringBuilder("INSERT INTO ").append(TABLE_RECOMMENDATIONS).append(" VALUES ");
        boolean somethingToInsert = false;

        LinkedHashMap<Integer, Boolean> alreadyInsertedRecommendations = new LinkedHashMap<>();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_RECOMMENDATIONS, new String[]{});
        while (cursor.moveToNext()) {
            alreadyInsertedRecommendations.put(cursor.getInt(0), true);
        }
        cursor.close();

        db.beginTransaction();

        for (SimilarNovel recommendation : recommendations) {
            if (alreadyInsertedRecommendations.get(recommendation.getNovelId()) == null) {
                query.append("(")
                        .append(recommendation.getNovelId()).append(",")
                        .append(recommendation.getPredictedRating()).append(",")
                        .append(formatString(recommendation.getTitle())).append(",")
                        .append(formatString(recommendation.getImage())).append(",")
                        .append(formatString(recommendation.getReleased()))
                        .append("),");
                somethingToInsert = true;
                alreadyInsertedRecommendations.put(recommendation.getNovelId(), true);
            }
        }

        if (somethingToInsert) {
            query.setLength(query.length() - 1);
            db.execSQL("DELETE FROM " + TABLE_RECOMMENDATIONS);
            db.execSQL(query.toString());
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static List<SimilarNovel> loadRecommendations(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        List<SimilarNovel> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_RECOMMENDATIONS, new String[]{});

        while (cursor.moveToNext()) {
            SimilarNovel similarNovel = new SimilarNovel();
            similarNovel.setNovelId(cursor.getInt(0));
            similarNovel.setPredictedRating(cursor.getDouble(1));
            similarNovel.setTitle(cursor.getString(2));
            similarNovel.setImage(cursor.getString(3));
            similarNovel.setReleased(cursor.getString(4));

            res.add(similarNovel);
        }

        cursor.close();
        return res;
    }

    public static List<SimilarNovel> loadSimilarNovels(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        List<SimilarNovel> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM  " + TABLE_SIMILAR_NOVELS + " WHERE vn = " + vnId, new String[]{});

        while (cursor.moveToNext()) {
            SimilarNovel similarNovel = new SimilarNovel();
            similarNovel.setNovelId(cursor.getInt(0));
            similarNovel.setSimilarity(cursor.getDouble(2));
            similarNovel.setTitle(cursor.getString(3));
            similarNovel.setImage(cursor.getString(4));

            res.add(similarNovel);
        }

        cursor.close();
        return res;
    }

    public static LinkedHashMap<Integer, VNlistItem> loadVnlist(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        LinkedHashMap<Integer, VNlistItem> res = new LinkedHashMap<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_VNLIST, new String[]{});
        while (cursor.moveToNext()) {
            res.put(cursor.getInt(0), new VNlistItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3)));
        }
        cursor.close();

        return res;
    }

    public static LinkedHashMap<Integer, VotelistItem> loadVotelist(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_VOTELIST, new String[]{});
        LinkedHashMap<Integer, VotelistItem> res = new LinkedHashMap<>();
        while (cursor.moveToNext()) {
            res.put(cursor.getInt(0), new VotelistItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));
        }
        cursor.close();

        return res;
    }

    public static LinkedHashMap<Integer, WishlistItem> loadWishlist(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_WISHLIST, new String[]{});
        LinkedHashMap<Integer, WishlistItem> res = new LinkedHashMap<>();
        while (cursor.moveToNext()) {
            res.put(cursor.getInt(0), new WishlistItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));
        }
        cursor.close();

        return res;
    }

    public static LinkedHashMap<Integer, Item> loadVns(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        LinkedHashMap<Integer, Item> res = new LinkedHashMap<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_VN, new String[]{});

        while (cursor.moveToNext()) {
            Item vn = new Item(cursor.getInt(0));
            vn.setTitle(cursor.getString(1));
            vn.setOriginal(cursor.getString(2));
            vn.setReleased(cursor.getString(3));
            vn.setAliases(cursor.getString(4));
            vn.setLength(cursor.getInt(5));
            vn.setDescription(cursor.getString(6));
            Links links = new Links();
            links.setWikipedia(cursor.getString(7));
            links.setEncubed(cursor.getString(8));
            links.setRenai(cursor.getString(9));
            vn.setLinks(links);
            vn.setImage(cursor.getString(10));
            vn.setImage_nsfw(cursor.getInt(11) == 1);
            vn.setPopularity(cursor.getDouble(12));
            vn.setRating(cursor.getDouble(13));
            vn.setVotecount(cursor.getInt(14));

            res.put(vn.getId(), vn);
        }

        cursor.close();
        return res;
    }

    public static List<String> loadLanguages(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        List<String> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_LANGUAGES + " WHERE vn = " + vnId, new String[]{});

        while (cursor.moveToNext()) {
            res.add(cursor.getString(1));
        }

        cursor.close();
        return res;
    }

    public static List<String> loadPlatforms(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        List<String> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PLATFORMS + " WHERE vn = " + vnId, new String[]{});

        while (cursor.moveToNext()) {
            res.add(cursor.getString(1));
        }

        cursor.close();
        return res;
    }

    public static List<Anime> loadAnimes(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        List<Anime> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_ANIME + " WHERE vn = " + vnId, new String[]{});

        while (cursor.moveToNext()) {
            Anime anime = new Anime();
            anime.setId(cursor.getInt(0));
            anime.setAnn_id(cursor.getInt(2));
            anime.setNfo_id(cursor.getString(3));
            anime.setTitle_romaji(cursor.getString(4));
            anime.setTitle_kanji(cursor.getString(5));
            anime.setYear(cursor.getInt(6));
            anime.setType(cursor.getString(7));
            res.add(anime);
        }

        cursor.close();
        return res;
    }

    public static List<Relation> loadRelations(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        List<Relation> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_RELATION + " WHERE vn = " + vnId, new String[]{});

        while (cursor.moveToNext()) {
            Relation relation = new Relation();
            relation.setId(cursor.getInt(0));
            relation.setRelation(cursor.getString(2));
            relation.setTitle(cursor.getString(3));
            relation.setOriginal(cursor.getString(4));
            res.add(relation);
        }

        cursor.close();
        return res;
    }

    public static List<List<Number>> loadTags(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        List<List<Number>> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_TAGS + " WHERE vn = " + vnId, new String[]{});

        while (cursor.moveToNext()) {
            List<Number> tag = new ArrayList<>();
            tag.add(cursor.getInt(0));
            tag.add(cursor.getInt(2));
            tag.add(cursor.getInt(3));
            res.add(tag);
        }

        cursor.close();
        return res;
    }

    public static List<VnStaff> loadStaff(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        List<VnStaff> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_VN_STAFF + " WHERE vid = " + vnId, new String[]{});

        while (cursor.moveToNext()) {
            VnStaff staff = new VnStaff();
            staff.setSid(cursor.getInt(1));
            staff.setAid(cursor.getInt(2));
            staff.setName(cursor.getString(3));
            staff.setOriginal(cursor.getString(4));
            staff.setRole(cursor.getString(5));
            staff.setNote(cursor.getString(6));
            res.add(staff);
        }

        cursor.close();
        return res;
    }

    public static List<Screen> loadScreens(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        List<Screen> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_SCREENS + " WHERE vn = " + vnId, new String[]{});

        while (cursor.moveToNext()) {
            Screen screen = new Screen();
            screen.setImage(cursor.getString(1));
            screen.setRid(cursor.getInt(2));
            screen.setNsfw(cursor.getInt(3) == 1);
            screen.setHeight(cursor.getInt(4));
            screen.setWidth(cursor.getInt(5));
            res.add(screen);
        }

        cursor.close();
        return res;
    }

    public static void clear(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        clear(db);
    }

    private static void clear(SQLiteDatabase db) {
        clearTable(db, TABLE_VNLIST);
        clearTable(db, TABLE_VOTELIST);
        clearTable(db, TABLE_WISHLIST);
        clearTable(db, TABLE_VN);
        clearTable(db, TABLE_LANGUAGES);
        clearTable(db, TABLE_ORIG_LANG);
        clearTable(db, TABLE_PLATFORMS);
        clearTable(db, TABLE_ANIME);
        clearTable(db, TABLE_RELATION);
        clearTable(db, TABLE_TAGS);
        clearTable(db, TABLE_SCREENS);
        clearTable(db, TABLE_VN_CHARACTER);
        clearTable(db, TABLE_CHARACTER);
        clearTable(db, TABLE_VN_STAFF);
        clearTable(db, TABLE_TRAITS);
        clearTable(db, TABLE_VN_RELEASE);
        clearTable(db, TABLE_RELEASE);
        clearTable(db, TABLE_RELEASE_LANGUAGES);
        clearTable(db, TABLE_RELEASE_PLATFORMS);
        clearTable(db, TABLE_RELEASE_MEDIA);
        clearTable(db, TABLE_RELEASE_PRODUCER);
        clearTable(db, TABLE_PRODUCER);
        clearTable(db, TABLE_SIMILAR_NOVELS);
        clearTable(db, TABLE_RECOMMENDATIONS);
    }

    private static void clearTable(SQLiteDatabase db, String table) {
        try {
            db.execSQL("DELETE FROM " + table);
        } catch (SQLiteException exception) {
            // The table doesn't exist, it's ok, just ignore it
        }
    }

    private static void exec(SQLiteDatabase db, StringBuilder[] queries, int[] itemsToInsert) {
        for (int i = 0; i < queries.length; i++) {
            if (itemsToInsert[i] > 0) {
                queries[i].setLength(queries[i].length() - 1);
                db.execSQL(queries[i].toString());
            }
        }
    }

    private static String formatString(String value) {
        return value == null ? null : "'" + value.replaceAll("\'", "''") + "'";
    }

    private static int formatBool(boolean value) {
        return value ? 1 : 0;
    }

    public static void startupClean(Context context, String listVnIds, Set<Integer> vnlistIds, Set<Integer> votelistIds, Set<Integer> wishlistIds) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        db.beginTransaction();

        db.execSQL("DELETE FROM " + TABLE_VNLIST + " WHERE vn NOT IN (" + TextUtils.join(",", vnlistIds) + ")");
        db.execSQL("DELETE FROM " + TABLE_VOTELIST + " WHERE vn NOT IN (" + TextUtils.join(",", votelistIds) + ")");
        db.execSQL("DELETE FROM " + TABLE_WISHLIST + " WHERE vn NOT IN (" + TextUtils.join(",", wishlistIds) + ")");

        String notInListVnIds = "NOT IN (" + listVnIds + ")";
        db.execSQL("DELETE FROM " + TABLE_VN + " WHERE id " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_LANGUAGES + " WHERE vn " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_ORIG_LANG + " WHERE vn " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_PLATFORMS + " WHERE vn " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_ANIME + " WHERE vn " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_RELATION + " WHERE vn " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_TAGS + " WHERE vn " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_SCREENS + " WHERE vn " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_VN_STAFF + " WHERE vid " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_VN_CHARACTER + " WHERE vn " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_VN_RELEASE + " WHERE vn " + notInListVnIds);
        db.execSQL("DELETE FROM " + TABLE_SIMILAR_NOVELS + " WHERE vn " + notInListVnIds);

        List<Integer> tmp = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT character FROM " + TABLE_VN_CHARACTER, new String[]{});
        while (cursor.moveToNext()) {
            tmp.add(cursor.getInt(0));
        }
        cursor.close();
        String existingCharacters = TextUtils.join(",", tmp);

        db.execSQL("DELETE FROM " + TABLE_CHARACTER + " WHERE id NOT IN (" + existingCharacters + ")");
        db.execSQL("DELETE FROM " + TABLE_TRAITS + " WHERE character NOT IN (" + existingCharacters + ")");

        tmp = new ArrayList<>();
        cursor = db.rawQuery("SELECT release FROM " + TABLE_VN_RELEASE, new String[]{});
        while (cursor.moveToNext()) {
            tmp.add(cursor.getInt(0));
        }
        cursor.close();
        String existingReleases = TextUtils.join(",", tmp);

        db.execSQL("DELETE FROM " + TABLE_RELEASE + " WHERE id NOT IN (" + existingReleases + ")");
        db.execSQL("DELETE FROM " + TABLE_RELEASE_LANGUAGES + " WHERE release NOT IN (" + existingReleases + ")");
        db.execSQL("DELETE FROM " + TABLE_RELEASE_PLATFORMS + " WHERE release NOT IN (" + existingReleases + ")");
        db.execSQL("DELETE FROM " + TABLE_RELEASE_MEDIA + " WHERE release NOT IN (" + existingReleases + ")");
        db.execSQL("DELETE FROM " + TABLE_RELEASE_PRODUCER + " WHERE release NOT IN (" + existingReleases + ")");
        db.execSQL("DELETE FROM " + TABLE_PRODUCER + " WHERE id NOT IN (SELECT producer FROM " + TABLE_RELEASE_PRODUCER + ")");

        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
