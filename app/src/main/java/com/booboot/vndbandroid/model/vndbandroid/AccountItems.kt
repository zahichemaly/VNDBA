package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.model.vndb.UserList
import com.booboot.vndbandroid.model.vndb.VN

data class AccountItems(
    var userList: Map<Long, UserList> = emptyMap(),
    var vns: Map<Long, VN> = emptyMap()
) {
    fun getLabelCount(vararg ids: Long) = userList.count { userList ->
        ids.any { id -> id in userList.value.labelIds() }
    }

    fun deepCopy() = copy(userList = userList.toMutableMap().apply {
        forEach { (id, vnlist) -> put(id, vnlist.copy()) }
    })
}