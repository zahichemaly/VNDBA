package com.booboot.vndbandroid.ui.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.extensions.asyncLazy
import com.booboot.vndbandroid.extensions.plusAssign
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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    protected suspend fun startupSyncInternal() = coroutineScope {
        val tagsJob = async { tagsRepository.getItems(CachePolicy(true)) }
        val traitsJob = async { traitsRepository.getItems(CachePolicy(true)) }
        val vnlistJob = async { vnlistRepository.getItems(CachePolicy(false)) }
        val votelistJob = async { votelistRepository.getItems(CachePolicy(false)) }
        val wishlistJob = async { wishlistRepository.getItems(CachePolicy(false)) }
        val oldVnlistJob = async { vnlistRepository.getItems(CachePolicy(cacheOnly = true)) }
        val oldVotelistJob = async { votelistRepository.getItems(CachePolicy(cacheOnly = true)) }
        val oldWishlistJob = async { wishlistRepository.getItems(CachePolicy(cacheOnly = true)) }

        val vnlist = vnlistJob.await()
        val votelist = votelistJob.await()
        val wishlist = wishlistJob.await()
        val oldVnlist = oldVnlistJob.await()
        val oldVotelist = oldVotelistJob.await()
        val oldWishlist = oldWishlistJob.await()

        val allIds = vnlist.keys.union(votelist.keys).union(wishlist.keys)
        val newIds = allIds.minus(oldVnlist.keys).minus(oldVotelist.keys).minus(oldWishlist.keys)
        val haveListsChanged = vnlist != oldVnlist || votelist != oldVotelist || wishlist != oldWishlist

        val hasAccountChanged = when {
            allIds.isEmpty() -> emptyMap() // empty account
            newIds.isNotEmpty() -> { // should send get vn
                vnRepository.getItems(newIds, FLAGS_DETAILS, CachePolicy(false))
            }
            haveListsChanged -> emptyMap() // no new VNs but status of existing VNs have changed
            else -> null // nothing new: skipping DB update with an empty result
        }?.let { vns ->
            boxStore.transaction(
                asyncLazy { vnlistRepository.setItems(vnlist) },
                asyncLazy { votelistRepository.setItems(votelist) },
                asyncLazy { wishlistRepository.setItems(wishlist) },
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