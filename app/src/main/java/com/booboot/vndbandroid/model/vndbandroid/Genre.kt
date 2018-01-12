package com.booboot.vndbandroid.model.vndbandroid

import java.util.*

object Genre {
    /**
     * List of existing genres, based on MAL advanced search
     */
    private val genres_array = arrayOf("Action", "Adventure", "Cars", "Comedy", "Dementia", "Demons", "Drama", "Ecchi", "Fantasy", "Game", "Harem", "Hentai", "Historical", "Horror", "Josei", "Kids",
            "Magic", "Martial Arts", "Mecha", "Military", "Music", "Mystery", "Parody", "Police", "Psychological", "Romance", "Samurai", "School", "Sci-Fi", "Seinen", "Shoujo", "Shoujo Ai",
            "Shounen", "Shounen Ai", "Slice of Life", "Space", "Sports", "Super Power", "Supernatural", "Thriller", "Vampire", "Yaoi", "Yuri")

    val GENRES = Arrays.asList(*genres_array)

    operator fun contains(tagName: String): Boolean {
        return GENRES.any { genre -> genre.toUpperCase() == tagName.trim().toUpperCase() }
    }
}