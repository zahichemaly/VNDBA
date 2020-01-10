package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.model.vndb.UserList
import io.objectbox.BoxStore
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.kotlin.boxFor
import io.objectbox.relation.ToMany

@Entity
class UserListDao() {
    @Id(assignable = true) var vn: Long = 0
    var added: Long = 0
    var lastmod: Long = 0
    var voted: Long? = null
    var vote: Int? = null
    var notes: String? = null
    var started: String? = null
    var finished: String? = null
    lateinit var labels: ToMany<LabelDao>

    constructor(userList: UserList, boxStore: BoxStore) : this() {
        vn = userList.vn
        added = userList.added
        lastmod = userList.lastmod
        voted = userList.voted
        vote = userList.vote
        notes = userList.notes
        started = userList.started
        finished = userList.finished

        boxStore.boxFor<UserListDao>().attach(this)
        userList.labels.forEach { labels.add(LabelDao(it)) }

        boxStore.boxFor<LabelDao>().put(labels)
    }

    fun toBo() = UserList(
        vn,
        added,
        lastmod,
        voted,
        vote,
        notes,
        started,
        finished,
        labels.map { it.toBo() }.toSet()
    )
}