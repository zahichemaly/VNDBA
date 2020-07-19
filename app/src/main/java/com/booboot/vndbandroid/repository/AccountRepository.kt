package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    var vnRepository: VNRepository,
    var userListRepository: UserListRepository,
    var userLabelsRepository: UserLabelsRepository
) {
    suspend fun getItems() = coroutineScope {
        val userLabelsJob = async { userLabelsRepository.getItems() }
        val userList = userListRepository.getItems()
        val vns = vnRepository.getItems(userList.keys, FLAGS_DETAILS)
        AccountItems(userList, userLabelsJob.await(), vns)
    }
}