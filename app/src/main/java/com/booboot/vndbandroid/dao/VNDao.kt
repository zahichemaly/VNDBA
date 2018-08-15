package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.VN
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.objectbox.BoxStore
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.kotlin.boxFor
import io.objectbox.relation.ToMany

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
    var tags: String = ""
    var popularity: Float = 0f
    var rating: Float = 0f
    var votecount: Int = 0
    lateinit var screens: ToMany<ScreenDao>

    constructor(vn: VN, boxStore: BoxStore, moshi: Moshi) : this() {
        id = vn.id
        title = vn.title
        original = vn.original
        released = vn.released
        aliases = vn.aliases
        length = vn.length
        description = vn.description
        image = vn.image
        image_nsfw = vn.image_nsfw
        tags = moshi
            .adapter<List<List<Float>>>(Types.newParameterizedType(List::class.java, Types.newParameterizedType(List::class.java, java.lang.Float::class.java)))
            .toJson(vn.tags)
        popularity = vn.popularity
        rating = vn.rating
        votecount = vn.votecount

        boxStore.boxFor<VNDao>().attach(this)
        vn.screens.forEach { screens.add(ScreenDao(it)) }
    }

    fun toBo(moshi: Moshi) = VN(
        id,
        title,
        original,
        released,
        aliases = aliases,
        length = length,
        description = description,
        image = image,
        image_nsfw = image_nsfw,
        tags = moshi
            .adapter<List<List<Float>>>(Types.newParameterizedType(List::class.java, Types.newParameterizedType(List::class.java, java.lang.Float::class.java)))
            .fromJson(tags)
            ?: emptyList(),
        popularity = popularity,
        rating = rating,
        votecount = votecount,
        screens = screens.map { it.toBo() }
    )
}