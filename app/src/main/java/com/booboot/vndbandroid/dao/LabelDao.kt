package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.Label
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class LabelDao() {
    @Id(assignable = true) var id: Long = 0
    var label: String = ""

    constructor(item: Label) : this() {
        id = item.id
        label = item.label
    }

    fun toBo() = Label(id, label)
}