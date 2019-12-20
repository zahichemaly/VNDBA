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
    var vnlistRepository: VnlistRepository,
    var votelistRepository: VotelistRepository,
    var wishlistRepository: WishlistRepository
) {
    suspend fun getItems() = coroutineScope {
        val vnlistJob = async { vnlistRepository.getItems() }
        val votelistJob = async { votelistRepository.getItems() }
        val wishlistJob = async { wishlistRepository.getItems() }
        val vnlist = vnlistJob.await()
        val votelist = votelistJob.await()
        val wishlist = wishlistJob.await()
        val vns = vnRepository.getItems(vnlist.keys.union(votelist.keys).union(wishlist.keys), FLAGS_DETAILS)
        AccountItems(vnlist, votelist, wishlist, vns)
    }
}