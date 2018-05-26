package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.di.Schedulers
import com.booboot.vndbandroid.model.vndb.AccountItems
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
        var db: DB,
        var vnRepository: VNRepository,
        var vnlistRepository: VnlistRepository,
        var votelistRepository: VotelistRepository,
        var wishlistRepository: WishlistRepository,
        var schedulers: Schedulers
) {
    fun getItems(): Single<AccountItems> = Single.fromCallable {
        AccountItems(
                vnlistRepository.getItems().blockingGet(),
                votelistRepository.getItems().blockingGet(),
                wishlistRepository.getItems().blockingGet(),
                vnRepository.getItems().blockingGet()
        )
    }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}