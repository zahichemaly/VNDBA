package com.booboot.vndbandroid.ui.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.extensions.transaction
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SyncData
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.CachePolicy
import com.booboot.vndbandroid.repository.TagsRepository
import com.booboot.vndbandroid.repository.TraitsRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.repository.VnlistRepository
import com.booboot.vndbandroid.repository.VotelistRepository
import com.booboot.vndbandroid.repository.WishlistRepository
import io.objectbox.BoxStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

abstract class StartupSyncViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnlistRepository: VnlistRepository
    @Inject lateinit var votelistRepository: VotelistRepository
    @Inject lateinit var wishlistRepository: WishlistRepository
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var tagsRepository: TagsRepository
    @Inject lateinit var traitsRepository: TraitsRepository
    @Inject lateinit var boxStore: BoxStore

    val accountData: MutableLiveData<AccountItems> = MutableLiveData()
    val syncData: MutableLiveData<SyncData> = MutableLiveData()

    protected suspend fun startupSync(coroutineScope: CoroutineScope, doOnAccountSuccess: (AccountItems) -> Unit = {}) = coroutineScope.async(Dispatchers.IO) {
        val tagsJob = tagsRepository.getItems(this, CachePolicy(true))
        val traitsJob = traitsRepository.getItems(this, CachePolicy(true))
        val vnlistJob = vnlistRepository.getItems(this, CachePolicy(false))
        val votelistJob = votelistRepository.getItems(this, CachePolicy(false))
        val wishlistJob = wishlistRepository.getItems(this, CachePolicy(false))
        val oldVnlistJob = vnlistRepository.getItems(this, CachePolicy(cacheOnly = true))
        val oldVotelistJob = votelistRepository.getItems(this, CachePolicy(cacheOnly = true))
        val oldWishlistJob = wishlistRepository.getItems(this, CachePolicy(cacheOnly = true))

        val vnlist = vnlistJob.await()
        val votelist = votelistJob.await()
        val wishlist = wishlistJob.await()
        val oldVnlist = oldVnlistJob.await()
        val oldVotelist = oldVotelistJob.await()
        val oldWishlist = oldWishlistJob.await()

        val allIds = vnlist.keys.union(votelist.keys).union(wishlist.keys)
        val newIds = allIds.minus(oldVnlist.keys).minus(oldVotelist.keys).minus(oldWishlist.keys)
        val haveListsChanged = vnlist != oldVnlist || votelist != oldVotelist || wishlist != oldWishlist

        val accountItems = when {
            allIds.isEmpty() -> emptyMap() // empty account
            newIds.isNotEmpty() -> { // should send get vn
                vnRepository.getItems(this, newIds, FLAGS_DETAILS, CachePolicy(false)).await()
            }
            haveListsChanged -> emptyMap() // no new VNs but status of existing VNs have changed
            else -> null // nothing new: skipping DB update with an empty result
        }?.let { vns ->
            boxStore.transaction(
                vnlistRepository.setItems(this, vnlist, true),
                votelistRepository.setItems(this, votelist, true),
                wishlistRepository.setItems(this, wishlist, true),
                vnRepository.setItems(this, vns, true)
            )
            // TODO DB startup clean (in a new flatmap, must make sure the above transaction has completed before)

            accountRepository.getItems(this).await().apply {
                accountData.postValue(this)
                doOnAccountSuccess(this)
            }
        } ?: accountRepository.getItems(this).await()

        Preferences.loggedIn = true
        syncData.postValue(SyncData(accountItems, tagsJob.await(), traitsJob.await()))
    }
}