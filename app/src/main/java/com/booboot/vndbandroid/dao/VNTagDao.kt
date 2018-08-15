package com.booboot.vndbandroid.dao

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class VNTagDao(
    @Id var id: Long = 0,
    var tagId: Int = 0,
    var score: Float = 0f,
    var spoilerLevel: Int = 0
) {
    constructor(tags: List<Float>) : this(
        tagId = tags[0].toInt(),
        score = tags[1],
        spoilerLevel = tags[2].toInt()
    )

    fun toBo() = listOf(tagId.toFloat(), score, spoilerLevel.toFloat())
}