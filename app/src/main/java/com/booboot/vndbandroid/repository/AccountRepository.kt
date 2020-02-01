package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    var vnRepository: VNRepository,
    var userListRepository: UserListRepository
) {
    suspend fun getItems(): AccountItems {
        val userList = userListRepository.getItems()
        val vns = vnRepository.getItems(userList.keys, FLAGS_DETAILS)
        return AccountItems(userList, vns)
    }
}