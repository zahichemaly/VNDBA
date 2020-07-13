package com.booboot.vndbandroid.model.vndbandroid

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
    fun filterLabel(vnId: Long, labelId: Long) = when {
        labelId < 0 -> true
        labelId in VOTELISTS -> Vote.outOf10(userList[vnId]?.vote).toLong() == voteIdToVote(labelId)
        else -> labelId in userList[vnId]?.labelIds() ?: setOf()
    }

    fun deepCopy() = copy(userList = userList.toMutableMap().apply {
        forEach { (id, vnlist) -> put(id, vnlist.copy()) }
    })
}