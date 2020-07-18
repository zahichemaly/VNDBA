package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.extensions.emptyOrAny
import com.booboot.vndbandroid.model.vndb.Label.Companion.NO_VOTE
import com.booboot.vndbandroid.model.vndb.Label.Companion.VOTED
import com.booboot.vndbandroid.model.vndb.Label.Companion.VOTELISTS
import com.booboot.vndbandroid.model.vndb.Label.Companion.voteIdToVote
import com.booboot.vndbandroid.model.vndb.UserLabel
import com.booboot.vndbandroid.model.vndb.UserList
import com.booboot.vndbandroid.model.vndb.VN

data class AccountItems(
    var userList: Map<Long, UserList> = emptyMap(),
    var userLabels: Map<Long, UserLabel> = emptyMap(),
    var vns: Map<Long, VN> = emptyMap()
) {
    fun filterLabel(vnId: Long, selectedFilters: Set<Long>): Boolean {
        /* Partitioning labels on which an "OR" should be used (the other partition will use "AND" */
        val (votes, others) = selectedFilters.partition { labelId -> labelId in VOTELISTS }
        return votes
            .emptyOrAny { labelId -> Vote.outOf10(userList[vnId]?.vote).toLong() == voteIdToVote(labelId) }
            .and(others.all { labelId ->
                when (labelId) {
                    NO_VOTE.id -> VOTED.id !in userList[vnId]?.labelIds() ?: setOf()
                    else -> labelId in userList[vnId]?.labelIds() ?: setOf()
                }
            })
    }

    fun deepCopy() = copy(
        userList = userList.toMutableMap().apply {
            forEach { (id, vnlist) -> put(id, vnlist.copy()) }
        },
        userLabels = userLabels.toMutableMap().apply {
            forEach { (id, label) -> put(id, label.copy()) }
        }
    )
}