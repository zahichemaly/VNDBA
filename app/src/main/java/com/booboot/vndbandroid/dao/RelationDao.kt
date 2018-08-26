package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.Relation
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class RelationDao() {
    @Id(assignable = true) var id: Long = 0
    var relation: String = ""
    var title: String = ""
    var original: String? = null
    var official: Boolean = true

    constructor(item: Relation) : this() {
        id = item.id
        relation = item.relation
        title = item.title
        original = item.original
        official = item.official
    }

    fun toBo() = Relation(id, relation, title, original, official)
}