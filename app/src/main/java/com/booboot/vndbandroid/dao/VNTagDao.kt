package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.extensions.generateUnique
import com.booboot.vndbandroid.model.vndb.VN
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
data class VNTagDao(
    @Id(assignable = true) var id: Long = 0,
    @Index var vnId: Long = 0,
    @Index var tagId: Long = 0,
    var score: Float = 0f,
    var spoilerLevel: Int = 0
) {
    constructor(tags: List<Float>, vn: VN) : this() {
        vnId = vn.id
        tagId = tags[0].toLong()
        score = tags[1]
        spoilerLevel = tags[2].toInt()
        id = vnId.generateUnique(tagId)
    }

    fun toBo() = listOf(tagId.toFloat(), score, spoilerLevel.toFloat())
}