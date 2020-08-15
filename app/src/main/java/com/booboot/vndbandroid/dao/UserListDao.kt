package com.booboot.vndbandroid.dao

import com.booboot.vndbandroid.extensions.removeRelations
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

        /**
         * attach() is mandatory because of self-assigned IDs, but also retrieves the ToMany relations from the DB.
         * If we simply add() new relations, they will be stacking up with the old ones; we don't want that, so we have to remove these relations.
         * /!\ Don't call clear() because it removes the related entities! Call remove() to only remove relations.
         * See https://docs.objectbox.io/relations#updating-tomany
         */
        boxStore.boxFor<UserListDao>().attach(this)
        labels.removeRelations()
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