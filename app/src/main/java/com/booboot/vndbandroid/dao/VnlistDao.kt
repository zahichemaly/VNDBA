package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.Vnlist
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class VnlistDao(
    @Id(assignable = true) var vn: Long = 0,
    var added: Int = 0,
    var status: Int = 0,
    var notes: String? = null
) {
    constructor(vnlist: Vnlist) : this(vnlist.vn, vnlist.added, vnlist.status, vnlist.notes)

    fun toBo() = Vnlist(vn, added, status, notes)
}