package com.booboot.vndbandroid.ui.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.di.BoxManager
import com.booboot.vndbandroid.extensions.asyncLazy
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.extensions.transaction
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SyncData
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.CachePolicy
import com.booboot.vndbandroid.repository.TagsRepository
import com.booboot.vndbandroid.repository.TraitsRepository
import com.booboot.vndbandroid.repository.UserLabelsRepository
import com.booboot.vndbandroid.repository.UserListRepository
import com.booboot.vndbandroid.repository.VNRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

abstract class StartupSyncViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var userListRepository: UserListRepository
    @Inject lateinit var userLabelsRepository: UserLabelsRepository
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var tagsRepository: TagsRepository
    @Inject lateinit var traitsRepository: TraitsRepository
    @Inject lateinit var boxManager: BoxManager

    val accountData: MutableLiveData<AccountItems> = MutableLiveData()
    val syncData: MutableLiveData<SyncData> = MutableLiveData()

    protected suspend fun startupSyncInternal() = coroutineScope {
        val tagsJob = async { tagsRepository.getItems(CachePolicy(true)) }
        val traitsJob = async { traitsRepository.getItems(CachePolicy(true)) }
        val userListJob = async { userListRepository.getItems(CachePolicy(false)) }
        val oldUserListJob = async { userListRepository.getItems(CachePolicy(cacheOnly = true)) }
        val userLabelsJob = async { userLabelsRepository.getItems(CachePolicy(false)) }

        val userList = userListJob.await()
        val oldUserList = oldUserListJob.await()
        userLabelsJob.await()

        val allIds = userList.keys
        val newIds = allIds.minus(oldUserList.keys)
        val hasListChanged = userList != oldUserList

        val hasAccountChanged = when {
            allIds.isEmpty() -> emptyMap() // empty account
            newIds.isNotEmpty() -> { // should send get vn
                vnRepository.getItems(newIds, FLAGS_DETAILS, CachePolicy(false))
            }
            hasListChanged -> emptyMap() // no new VNs but status of existing VNs have changed
            else -> null // nothing new: skipping DB update with an empty result
        }?.let { vns ->
            boxManager.boxStore.transaction(
                asyncLazy { userListRepository.setItems(userList) },
                asyncLazy { vnRepository.setItems(vns) }
            )
            true
        } ?: false

        // TODO DB startup clean in ALL cases exactly here on this line
        val accountItems = accountRepository.getItems()
        if (hasAccountChanged) accountData += accountItems

        Preferences.loggedIn = true
        syncData += SyncData(accountItems, tagsJob.await(), traitsJob.await())
    }
}