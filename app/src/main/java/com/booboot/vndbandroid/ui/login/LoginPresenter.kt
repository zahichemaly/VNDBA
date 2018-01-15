package com.booboot.vndbandroid.ui.login

import android.text.TextUtils
import com.booboot.vndbandroid.BuildConfig
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.di.Schedulers
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.VNlistItem
import com.booboot.vndbandroid.model.vndbandroid.VotelistItem
import com.booboot.vndbandroid.model.vndbandroid.WishlistItem
import com.booboot.vndbandroid.ui.Presenter
import com.booboot.vndbandroid.util.type
import io.reactivex.Single
import io.reactivex.functions.Function3
import javax.inject.Inject

open class LoginPresenter @Inject constructor(
        private val vndbServer: VNDBServer,
        private val schedulers: Schedulers) : Presenter<LoginView>() {

    fun login() {
        val vnlistIds = vndbServer.get<VNlistItem>("vnlist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true), type()).subscribeOn(schedulers.newThread())
        val votelistIds = vndbServer.get<VotelistItem>("votelist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true, socketIndex = 1), type()).subscribeOn(schedulers.newThread())
        val wishlistIds = vndbServer.get<WishlistItem>("wishlist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true, socketIndex = 2), type()).subscribeOn(schedulers.newThread())

        val observable = Single.zip(vnlistIds, votelistIds, wishlistIds,
                Function3<Results<VNlistItem>, Results<VotelistItem>, Results<WishlistItem>, AccountItems> { vni, vti, wsi ->
                    AccountItems(vni.items, vti.items, wsi.items)
                })
                .observeOn(schedulers.ui())
                .doOnSubscribe { view?.showLoading(true) }
                .observeOn(schedulers.io())
                .flatMap { items: AccountItems ->
                    val mergedIds = items.vnlist.map { it.vn }.toMutableSet()
                    mergedIds.addAll(items.votelist.map { it.vn })
                    mergedIds.addAll(items.wishlist.map { it.vn })
                    val mergedIdsString = TextUtils.join(",", mergedIds)
                    Single.just(mergedIds)
                }
                .observeOn(schedulers.ui())
                .doFinally { view?.showLoading(false) }
                .subscribe(::onNext, ::onError)
        composite.add(observable)
    }

    private fun onNext(result: MutableSet<Int>) {
        view?.showResult(result)
    }

    private fun onError(throwable: Throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace()
        view?.showError(throwable.localizedMessage)
    }
}