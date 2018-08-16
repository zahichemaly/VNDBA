package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.VN
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
    var popularity: Float = 0f
    var rating: Float = 0f
    var votecount: Int = 0
    lateinit var tags: ToMany<VNTagDao>
    lateinit var screens: ToMany<ScreenDao>

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

        boxStore.boxFor<VNDao>().attach(this)
        vn.tags.forEach { tags.add(VNTagDao(it)) }
        vn.screens.forEach { screens.add(ScreenDao(it)) }
    }

    fun toBo(fetchAll: Boolean = false): VN = VN(
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
        votecount = votecount
    ).apply {
        if (fetchAll) {
            tags = this@VNDao.tags.map { it.toBo() }
            screens = this@VNDao.screens.map { it.toBo() }
        }
    }
}