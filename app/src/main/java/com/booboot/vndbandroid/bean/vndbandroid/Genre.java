package com.booboot.vndbandroid.bean.vndbandroid;

import java.util.Arrays;
import java.util.List;

/**
 * Created by od on 15/03/2016.
 */
public class Genre {
    /**
     * List of existing genres, based on MAL advanced search
     */
    private final static String[] genres_array = new String[]{
            "Action", "Adventure", "Cars", "Comedy", "Dementia", "Demons", "Drama", "Ecchi",
            "Fantasy", "Game", "Harem", "Hentai", "Historical", "Horror", "Josei", "Kids", "Magic",
            "Martial Arts", "Mecha", "Military", "Music", "Mystery", "Parody", "Police", "Psychological",
            "Romance", "Samurai", "School", "Sci-Fi", "Seinen", "Shoujo", "Shoujo Ai", "Shounen",
            "Shounen Ai", "Slice of Life", "Space", "Sports", "Super Power", "Supernatural", "Thriller",
            "Vampire", "Yaoi", "Yuri"
    };

    public final static List<String> GENRES = Arrays.asList(genres_array);

    public static boolean contains(String tagName) {
        for (String genre : GENRES) {
            if (genre.toUpperCase().equals(tagName.trim().toUpperCase())) {
                return true;
            }
        }

        return false;
    }
}
