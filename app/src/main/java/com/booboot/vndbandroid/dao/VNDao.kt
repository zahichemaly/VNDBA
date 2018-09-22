package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.ApiFlags
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_FULL
import io.objectbox.BoxStore
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.kotlin.boxFor
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import kotlin.math.min

@Entity
class VNDao() {
    @Id(assignable = true) var id: Long = 0
    var title: String = ""
    var original: String? = null
    var released: String? = null
    var aliases: String? = null
    var length: Int? = 0
    var description: String? = null
    var image: String? = null
    var image_nsfw: Boolean = false
    var popularity: Float = 0f
    var rating: Float = 0f
    var votecount: Int = 0
    var flags: Int = 0
    lateinit var languages: ToMany<LanguageDao>
    lateinit var orig_lang: ToMany<OriginalLanguageDao>
    lateinit var platforms: ToMany<PlatformDao>
    lateinit var tags: ToMany<VNTagDao>
    lateinit var screens: ToMany<ScreenDao>
    lateinit var links: ToOne<LinksDao>
    lateinit var anime: ToMany<AnimeDao>
    lateinit var relations: ToMany<RelationDao>

    constructor(vn: VN, boxStore: BoxStore) : this() {
        id = vn.id
        title = vn.title
        original = vn.original
        released = vn.released
        aliases = vn.aliases
        length = vn.length
        description = vn.description
        image = vn.image
        image_nsfw = vn.image_nsfw
        popularity = vn.popularity
        rating = vn.rating
        votecount = vn.votecount
        flags = vn.flags

        boxStore.boxFor<VNDao>().attach(this)
        vn.languages.forEach { languages.add(LanguageDao(it.hashCode().toLong(), it)) }
        vn.orig_lang.forEach { orig_lang.add(OriginalLanguageDao(it.hashCode().toLong(), it)) }
        vn.platforms.forEach { platforms.add(PlatformDao(it.hashCode().toLong(), it)) }
        vn.tags.forEach { tags.add(VNTagDao(it, vn)) }
        vn.screens.forEach { screens.add(ScreenDao(it)) }
        vn.anime.forEach { anime.add(AnimeDao(it)) }
        vn.relations.forEach { relations.add(RelationDao(it)) }
        links.target = LinksDao(vn.links)

        boxStore.boxFor<LanguageDao>().put(languages)
        boxStore.boxFor<OriginalLanguageDao>().put(orig_lang)
        boxStore.boxFor<PlatformDao>().put(platforms)
        boxStore.boxFor<VNTagDao>().put(tags)
        boxStore.boxFor<ScreenDao>().put(screens)
        boxStore.boxFor<AnimeDao>().put(anime)
        boxStore.boxFor<RelationDao>().put(relations)
    }

    fun toBo(@ApiFlags content: Int = FLAGS_DETAILS): VN = VN(
        id,
        title,
        original,
        released,
        aliases = aliases,
        length = length,
        description = description,
        image = image,
        image_nsfw = image_nsfw,
        popularity = popularity,
        rating = rating,
        votecount = votecount,
        flags = min(flags, FLAGS_DETAILS)
    ).apply {
        if (content == FLAGS_FULL) {
            languages = this@VNDao.languages.map { it.value }
            orig_lang = this@VNDao.orig_lang.map { it.value }
            platforms = this@VNDao.platforms.map { it.value }
            tags = this@VNDao.tags.map { it.toBo() }
            screens = this@VNDao.screens.map { it.toBo() }
            links = this@VNDao.links.target.toBo()
            anime = this@VNDao.anime.map { it.toBo() }
            relations = this@VNDao.relations.map { it.toBo() }
            flags = min(this@VNDao.flags, FLAGS_FULL)
        }
    }
}

@Entity
data class LanguageDao(
    @Id(assignable = true) var id: Long = 0,
    var value: String = ""
)

@Entity
data class OriginalLanguageDao(
    @Id(assignable = true) var id: Long = 0,
    var value: String = ""
)

@Entity
data class PlatformDao(
    @Id(assignable = true) var id: Long = 0,
    var value: String = ""
)