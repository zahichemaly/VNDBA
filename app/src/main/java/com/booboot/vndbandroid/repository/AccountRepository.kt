package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    var vnRepository: VNRepository,
    var userListRepository: UserListRepository
) {
    suspend fun getItems() = coroutineScope {
        val userListJob = async { userListRepository.getItems() }
        val userList = userListJob.await()
        val vns = vnRepository.getItems(userList.keys, FLAGS_DETAILS)
        AccountItems(userList, vns)
    }
}