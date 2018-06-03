package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.model.vndb.AccountItems
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
        var db: DB,
        var vnRepository: VNRepository,
        var vnlistRepository: VnlistRepository,
        var votelistRepository: VotelistRepository,
        var wishlistRepository: WishlistRepository
) {
    fun getItems(): Single<AccountItems> = Single.fromCallable {
        AccountItems(
                vnlistRepository.getItems().blockingGet(),
                votelistRepository.getItems().blockingGet(),
                wishlistRepository.getItems().blockingGet(),
                vnRepository.getItems().blockingGet()
        )
    }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}