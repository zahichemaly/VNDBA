package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.model.vndb.Wishlist
import io.reactivex.Single
import io.reactivex.functions.Function4
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
    fun getItems(): Single<AccountItems> = Single.zip(
        vnlistRepository.getItems(),
        votelistRepository.getItems(),
        wishlistRepository.getItems(),
        vnRepository.getItems().subscribeOn(Schedulers.newThread()),
        Function4<Map<Long, Vnlist>, Map<Long, Votelist>, Map<Long, Wishlist>, Map<Long, VN>, AccountItems> { vni, vti, wsi, vn ->
            AccountItems(vni, vti, wsi, vn)
        }).subscribeOn(Schedulers.io())
}