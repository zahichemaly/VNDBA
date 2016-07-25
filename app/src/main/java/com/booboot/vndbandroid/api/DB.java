package com.booboot.vndbandroid.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.booboot.vndbandroid.bean.Anime;
import com.booboot.vndbandroid.bean.Item;
import com.booboot.vndbandroid.bean.Links;
import com.booboot.vndbandroid.bean.Media;
import com.booboot.vndbandroid.bean.Producer;
import com.booboot.vndbandroid.bean.Relation;
import com.booboot.vndbandroid.bean.Screen;
import com.booboot.vndbandroid.bean.cache.VNlistItem;
import com.booboot.vndbandroid.bean.cache.VotelistItem;
import com.booboot.vndbandroid.bean.cache.WishlistItem;
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
    public final static String TABLE_SCREENS = "screens";
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public static void saveVnlist(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_VNLIST);

        StringBuilder query = new StringBuilder("INSERT INTO ").append(TABLE_VNLIST).append(" VALUES ");
        boolean somethingToInsert = false;
        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        LinkedHashMap<Integer, VNlistItem> alreadyInsertedItems = loadVnlist(context);
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
            somethingToInsert = true;
        }

        if (somethingToInsert) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void saveVotelist(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_VNLIST);

        StringBuilder query = new StringBuilder("INSERT INTO ").append(TABLE_VOTELIST).append(" VALUES ");
        boolean somethingToInsert = false;
        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        LinkedHashMap<Integer, VotelistItem> alreadyInsertedItems = loadVotelist(context);
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
            somethingToInsert = true;
        }

        if (somethingToInsert) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void saveWishlist(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_VNLIST);

        StringBuilder query = new StringBuilder("INSERT INTO ").append(TABLE_WISHLIST).append(" VALUES ");
        boolean somethingToInsert = false;
        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        LinkedHashMap<Integer, WishlistItem> alreadyInsertedItems = loadWishlist(context);
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
            somethingToInsert = true;
        }

        if (somethingToInsert) {
            query.setLength(query.length() - 1);
            db.execSQL(query.toString());
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void saveVNs(Context context) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_VNLIST);

        boolean somethingToInsert[] = new boolean[8];
        StringBuilder[] queries = new StringBuilder[8];
        queries[0] = new StringBuilder("INSERT INTO ").append(TABLE_VN).append(" VALUES ");
        queries[1] = new StringBuilder("INSERT INTO ").append(TABLE_LANGUAGES).append(" VALUES ");
        queries[2] = new StringBuilder("INSERT INTO ").append(TABLE_ORIG_LANG).append(" VALUES ");
        queries[3] = new StringBuilder("INSERT INTO ").append(TABLE_PLATFORMS).append(" VALUES ");
        queries[4] = new StringBuilder("INSERT INTO ").append(TABLE_ANIME).append(" VALUES ");
        queries[5] = new StringBuilder("INSERT INTO ").append(TABLE_RELATION).append(" VALUES ");
        queries[6] = new StringBuilder("INSERT INTO ").append(TABLE_TAGS).append(" VALUES ");
        queries[7] = new StringBuilder("INSERT INTO ").append(TABLE_SCREENS).append(" VALUES ");

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
            somethingToInsert[0] = true;

            for (String language : item.getLanguages()) {
                queries[1].append("(")
                        .append(item.getId()).append(",")
                        .append(formatString(language))
                        .append("),");
                somethingToInsert[1] = true;
            }

            for (String origLang : item.getOrig_lang()) {
                queries[2].append("(")
                        .append(item.getId()).append(",")
                        .append(formatString(origLang))
                        .append("),");
                somethingToInsert[2] = true;
            }

            for (String platform : item.getPlatforms()) {
                queries[3].append("(")
                        .append(item.getId()).append(",")
                        .append(formatString(platform))
                        .append("),");
                somethingToInsert[3] = true;
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
                somethingToInsert[4] = true;
            }

            for (Relation relation : item.getRelations()) {
                queries[5].append("(")
                        .append(relation.getId()).append(",")
                        .append(item.getId()).append(",")
                        .append(formatString(relation.getRelation())).append(",")
                        .append(formatString(relation.getTitle())).append(",")
                        .append(formatString(relation.getOriginal()))
                        .append("),");
                somethingToInsert[5] = true;
            }

            for (List<Number> tagInfo : item.getTags()) {
                queries[6].append("(")
                        .append(tagInfo.get(0)).append(",")
                        .append(item.getId()).append(",")
                        .append(tagInfo.get(1)).append(",")
                        .append(tagInfo.get(2))
                        .append("),");
                somethingToInsert[6] = true;
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
                somethingToInsert[7] = true;
            }
        }

        exec(db, queries, somethingToInsert);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void saveCharacters(Context context, List<Item> characters, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_VNLIST);

        StringBuilder[] queries = new StringBuilder[3];
        queries[0] = new StringBuilder("INSERT INTO ").append(TABLE_VN_CHARACTER).append(" VALUES ");
        queries[1] = new StringBuilder("INSERT INTO ").append(TABLE_CHARACTER).append(" VALUES ");
        queries[2] = new StringBuilder("INSERT INTO ").append(TABLE_TRAITS).append(" VALUES ");
        boolean[] somethingToInsert = new boolean[3];

        /* Retrieving all items to check if we have TO INSERT or UPDATE */
        LinkedHashMap<Integer, Boolean> alreadyInsertedItems = new LinkedHashMap<>();
        Set<Integer> characterIds = new HashSet<>();
        for (Item character : characters) {
            characterIds.add(character.getId());
        }

        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_CHARACTER + " WHERE id IN (" + TextUtils.join(",", characterIds) + ")", new String[]{});
        while (cursor.moveToNext()) {
            alreadyInsertedItems.put(cursor.getInt(0), true);
        }
        cursor.close();
        db.beginTransaction();

        for (Item character : characters) {
            if (alreadyInsertedItems.get(character.getId()) == null) {
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
                somethingToInsert[1] = true;

                for (int[] trait : character.getTraits()) {
                    queries[2].append("(")
                            .append(character.getId()).append(",")
                            .append(trait[0]).append(",")
                            .append(trait[1])
                            .append("),");
                    somethingToInsert[2] = true;
                }
            }

            queries[0].append("(")
                    .append(vnId).append(",")
                    .append(character.getId())
                    .append("),");
            somethingToInsert[0] = true;
        }

        exec(db, queries, somethingToInsert);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static List<Item> loadCharacters(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        LinkedHashMap<Integer, Item> res = new LinkedHashMap<>(); // vn -> character

        Cursor[] cursor = new Cursor[2];
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

        for (Cursor c : cursor) c.close();

        return new ArrayList<>(res.values());
    }

    public static void saveReleases(Context context, List<Item> releases, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_VNLIST);

        StringBuilder[] queries = new StringBuilder[7];
        queries[0] = new StringBuilder("INSERT INTO ").append(TABLE_VN_RELEASE).append(" VALUES ");
        queries[1] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE).append(" VALUES ");
        queries[2] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE_LANGUAGES).append(" VALUES ");
        queries[3] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE_PLATFORMS).append(" VALUES ");
        queries[4] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE_MEDIA).append(" VALUES ");
        queries[5] = new StringBuilder("INSERT INTO ").append(TABLE_RELEASE_PRODUCER).append(" VALUES ");
        queries[6] = new StringBuilder("INSERT INTO ").append(TABLE_PRODUCER).append(" VALUES ");
        boolean[] somethingToInsert = new boolean[7];

        /* Retrieving all items to check if we have to INSERT or UPDATE */
        LinkedHashMap<Integer, Boolean> alreadyInsertedReleases = new LinkedHashMap<>();
        LinkedHashMap<Integer, Boolean> alreadyInsertedProducers = new LinkedHashMap<>();
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
        db.beginTransaction();

        for (Item release : releases) {
            if (alreadyInsertedReleases.get(release.getId()) == null) {
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
                somethingToInsert[1] = true;
                alreadyInsertedReleases.put(release.getId(), true);

                for (String language : release.getLanguages()) {
                    queries[2].append("(")
                            .append(release.getId()).append(",")
                            .append(formatString(language))
                            .append("),");
                    somethingToInsert[2] = true;
                }

                for (String platform : release.getPlatforms()) {
                    queries[3].append("(")
                            .append(release.getId()).append(",")
                            .append(formatString(platform))
                            .append("),");
                    somethingToInsert[3] = true;
                }

                for (Media media : release.getMedia()) {
                    queries[4].append("(")
                            .append(release.getId()).append(",")
                            .append(formatString(media.getMedium())).append(",")
                            .append(media.getQty())
                            .append("),");
                    somethingToInsert[4] = true;
                }

                for (Producer producer : release.getProducers()) {
                    queries[5].append("(")
                            .append(release.getId()).append(",")
                            .append(producer.getId()).append(",")
                            .append(formatBool(producer.isDeveloper())).append(",")
                            .append(formatBool(producer.isPublisher()))
                            .append("),");
                    somethingToInsert[5] = true;

                    if (alreadyInsertedProducers.get(producer.getId()) == null) {
                        queries[6].append("(")
                                .append(producer.getId()).append(",")
                                .append(formatString(producer.getName())).append(",")
                                .append(formatString(producer.getOriginal())).append(",")
                                .append(formatString(producer.getType()))
                                .append("),");
                        somethingToInsert[6] = true;
                        alreadyInsertedProducers.put(producer.getId(), true);
                    }
                }
            }

            queries[0].append("(")
                    .append(vnId).append(",")
                    .append(release.getId())
                    .append("),");
            somethingToInsert[0] = true;
        }

        exec(db, queries, somethingToInsert);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static List<Item> loadReleases(Context context, int vnId) {
        if (instance == null) instance = new DB(context);
        SQLiteDatabase db = instance.getWritableDatabase();

        LinkedHashMap<Integer, Item> res = new LinkedHashMap<>(); // vn -> release

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

            res.put(cursor[0].getInt(0), release);
        }

        String releasesIds = TextUtils.join(",", res.keySet());
        cursor[1] = db.rawQuery("SELECT * FROM " + TABLE_RELEASE_LANGUAGES + " WHERE release IN (" + releasesIds + ")", new String[]{});
        while (cursor[1].moveToNext()) {
            Item release = res.get(cursor[1].getInt(0));
            if (release == null) continue;
            if (release.getLanguages() == null)
                release.setLanguages(new ArrayList<String>());
            release.getLanguages().add(cursor[1].getString(1));
        }

        cursor[2] = db.rawQuery("SELECT * FROM " + TABLE_RELEASE_PLATFORMS + " WHERE release IN (" + releasesIds + ")", new String[]{});
        while (cursor[2].moveToNext()) {
            Item release = res.get(cursor[2].getInt(0));
            if (release == null) continue;
            if (release.getPlatforms() == null)
                release.setPlatforms(new ArrayList<String>());
            release.getPlatforms().add(cursor[2].getString(1));
        }

        cursor[3] = db.rawQuery("SELECT * FROM " + TABLE_RELEASE_MEDIA + " WHERE release IN (" + releasesIds + ")", new String[]{});
        while (cursor[3].moveToNext()) {
            Item release = res.get(cursor[3].getInt(0));
            if (release == null) continue;
            if (release.getMedia() == null)
                release.setMedia(new ArrayList<Media>());
            release.getMedia().add(new Media(cursor[3].getString(1), cursor[3].getInt(2)));
        }

        cursor[4] = db.rawQuery("SELECT * FROM  " + TABLE_RELEASE_PRODUCER + " rp INNER JOIN " + TABLE_PRODUCER + " p ON rp.producer = p.id WHERE rp.release IN (" + releasesIds + ")", new String[]{});
        while (cursor[4].moveToNext()) {
            Item release = res.get(cursor[4].getInt(0));
            if (release == null) continue;
            if (release.getProducers() == null)
                release.setProducers(new ArrayList<Producer>());
            release.getProducers().add(new Producer(cursor[4].getInt(1), cursor[4].getInt(2) == 1, cursor[4].getInt(3) == 1, cursor[4].getString(4), cursor[4].getString(5), cursor[4].getString(6)));
        }

        for (Cursor c : cursor) c.close();

        return new ArrayList<>(res.values());
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

        db.execSQL("DELETE FROM " + TABLE_VNLIST);
        db.execSQL("DELETE FROM " + TABLE_VOTELIST);
        db.execSQL("DELETE FROM " + TABLE_WISHLIST);
        db.execSQL("DELETE FROM " + TABLE_VN);
        db.execSQL("DELETE FROM " + TABLE_LANGUAGES);
        db.execSQL("DELETE FROM " + TABLE_ORIG_LANG);
        db.execSQL("DELETE FROM " + TABLE_PLATFORMS);
        db.execSQL("DELETE FROM " + TABLE_ANIME);
        db.execSQL("DELETE FROM " + TABLE_RELATION);
        db.execSQL("DELETE FROM " + TABLE_TAGS);
        db.execSQL("DELETE FROM " + TABLE_SCREENS);
        db.execSQL("DELETE FROM " + TABLE_VN_CHARACTER);
        db.execSQL("DELETE FROM " + TABLE_CHARACTER);
        db.execSQL("DELETE FROM " + TABLE_TRAITS);
        db.execSQL("DELETE FROM " + TABLE_VN_RELEASE);
        db.execSQL("DELETE FROM " + TABLE_RELEASE);
        db.execSQL("DELETE FROM " + TABLE_RELEASE_LANGUAGES);
        db.execSQL("DELETE FROM " + TABLE_RELEASE_PLATFORMS);
        db.execSQL("DELETE FROM " + TABLE_RELEASE_MEDIA);
        db.execSQL("DELETE FROM " + TABLE_RELEASE_PRODUCER);
        db.execSQL("DELETE FROM " + TABLE_PRODUCER);
    }

    private static void exec(SQLiteDatabase db, StringBuilder[] queries, boolean[] somethingToInsert) {
        for (int i = 0; i < queries.length; i++) {
            if (somethingToInsert[i]) {
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
}
