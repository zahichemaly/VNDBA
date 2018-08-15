package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.Votelist
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class VotelistDao(
    @Id(assignable = true) var vn: Long = 0,
    var added: Int = 0,
    var vote: Int = 0
) {
    constructor(votelist: Votelist) : this(votelist.vn, votelist.added, votelist.vote)

    fun toBo() = Votelist(vn, added, vote)
}