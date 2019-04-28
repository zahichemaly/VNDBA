package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    var vnRepository: VNRepository,
    var vnlistRepository: VnlistRepository,
    var votelistRepository: VotelistRepository,
    var wishlistRepository: WishlistRepository
) {
    fun getItems(coroutineScope: CoroutineScope) = coroutineScope.async(Dispatchers.IO) {
        val vnlistJob = vnlistRepository.getItems(this)
        val votelistJob = votelistRepository.getItems(this)
        val wishlistJob = wishlistRepository.getItems(this)
        val vnlist = vnlistJob.await()
        val votelist = votelistJob.await()
        val wishlist = wishlistJob.await()
        val vns = vnRepository.getItems(this, vnlist.keys.union(votelist.keys).union(wishlist.keys), FLAGS_DETAILS).await()
        AccountItems(vnlist, votelist, wishlist, vns)
    }
}