package com.booboot.vndbandroid.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.booboot.vndbandroid.bean.cache.VNlistItem;
import com.booboot.vndbandroid.bean.cache.VotelistItem;
import com.booboot.vndbandroid.bean.cache.WishlistItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by od on 05/07/2016.
 */
public class DB extends SQLiteOpenHelper {
    public static DB instance;
    public final static String NAME = "VNDB_ANDROID";
    public final static int VERSION = 1;

    public final static String TABLE_VNLIST = "vnlist";
    public final static String TABLE_VOTELIST = "votelist";
    public final static String TABLE_WISHLIST = "wishlist";
    public final static String TABLE_VN = "vn";
    public final static String TABLE_LANGUAGES = "languages";
    public final static String TABLE_ORIG_LANG = "orig_lang";
    public final static String TABLE_PLATFORMS = "platforms";
    public final static String TABLE_ANIME = "anime";
    public final static String TABLE_RELATION = "relation";
    public final static String TABLE_TAGS = "tags";
    public final static String TABLE_VN_CHARACTER = "vn_character";
    public final static String TABLE_CHARACTER = "character";
    public final static String TABLE_TRAITS = "traits";
    public final static String TABLE_VN_RELEASE = "vn_release";
    public final static String TABLE_RELEASE = "release";
    public final static String TABLE_RELEASE_LANGUAGES = "release_languages";
    public final static String TABLE_RELEASE_PLATFORMS = "release_platforms";
    public final static String TABLE_RELEASE_MEDIA = "release_media";
    public final static String TABLE_RELEASE_PRODUCER = "release_producer";
    public final static String TABLE_PRODUCER = "producer";

    public DB(Context context) {
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
                "weight INTEGER " +
                "vns TEXT " + // serialized JSON
                ")");

        db.execSQL("CREATE TABLE " + TABLE_TRAITS + " (" +
                "character INTEGER, " +
                "trait TEXT, " +
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public static void saveVnlist(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_VNLIST);

        StringBuilder query = new StringBuilder();

        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        Cursor cursor = db.rawQuery("select * from " + TABLE_VNLIST, new String[]{});
        Map<Integer, VNlistItem> alreadyInsertedItems = new HashMap<>();
        while (cursor.moveToNext()) {
            alreadyInsertedItems.put(cursor.getInt(0), new VNlistItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3)));
        }
        cursor.close();

        boolean somethingToInsert = false;
        query.append("INSERT INTO ").append(TABLE_VNLIST).append(" VALUES ");
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
            somethingToInsert = true;
        }

        if (somethingToInsert) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }
    }

    public static void saveVotelist(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_VNLIST);

        StringBuilder query = new StringBuilder();

        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        Cursor cursor = db.rawQuery("select * from " + TABLE_VOTELIST, new String[]{});
        Map<Integer, VotelistItem> alreadyInsertedItems = new HashMap<>();
        while (cursor.moveToNext()) {
            alreadyInsertedItems.put(cursor.getInt(0), new VotelistItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));
        }
        cursor.close();

        boolean somethingToInsert = false;
        query.append("INSERT INTO ").append(TABLE_VNLIST).append(" VALUES ");
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
                    .append(item.getVote()).append(",")
                    .append("),");
            somethingToInsert = true;
        }

        if (somethingToInsert) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }
    }

    public static void saveWishlist(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_VNLIST);

        StringBuilder query = new StringBuilder();

        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        Cursor cursor = db.rawQuery("select * from " + TABLE_WISHLIST, new String[]{});
        Map<Integer, WishlistItem> alreadyInsertedItems = new HashMap<>();
        while (cursor.moveToNext()) {
            alreadyInsertedItems.put(cursor.getInt(0), new WishlistItem(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));
        }
        cursor.close();

        boolean somethingToInsert = false;
        query.append("INSERT INTO ").append(TABLE_WISHLIST).append(" VALUES ");
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
                    .append(item.getPriority()).append(",")
                    .append("),");
            somethingToInsert = true;
        }

        if (somethingToInsert) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }
    }

    private static String formatString(String value) {
        return value == null ? value : "'" + value + "'";
    }
}
