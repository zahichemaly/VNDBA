package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    var vnRepository: VNRepository,
    var vnlistRepository: VnlistRepository,
    var votelistRepository: VotelistRepository,
    var wishlistRepository: WishlistRepository
) {
    fun getItems(): Single<AccountItems> {
        var items = AccountItems()

        return Single.zip(
            vnlistRepository.getItems(),
            votelistRepository.getItems(),
            wishlistRepository.getItems(),
            Function3<Map<Long, Vnlist>, Map<Long, Votelist>, Map<Long, Wishlist>, AccountItems> { vni, vti, wsi ->
                items = AccountItems(vni, vti, wsi)
                items
            })
            .observeOn(Schedulers.io())
            .flatMap {
                vnRepository.getItems(it.vnlist.keys.union(it.votelist.keys).union(it.wishlist.keys), FLAGS_DETAILS)
            }
            .map {
                items.vns = it
                items
            }
            .subscribeOn(Schedulers.io())
    }
}