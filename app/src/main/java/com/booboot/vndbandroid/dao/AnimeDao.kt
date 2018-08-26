package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.Anime
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class AnimeDao() {
    @Id(assignable = true) var id: Long = 0
    var ann_id: Int? = 0
    var nfo_id: String? = null
    var title_romaji: String? = null
    var title_kanji: String? = null
    var year: Int? = 0
    var type: String? = null

    constructor(anime: Anime) : this() {
        id = anime.id
        ann_id = anime.ann_id
        nfo_id = anime.nfo_id
        title_romaji = anime.title_romaji
        title_kanji = anime.title_kanji
        year = anime.year
        type = anime.type
    }

    fun toBo() = Anime(id, ann_id, nfo_id, title_romaji, title_kanji, year, type)
}