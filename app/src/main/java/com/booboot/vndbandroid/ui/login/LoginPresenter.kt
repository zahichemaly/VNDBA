package com.booboot.vndbandroid.ui.login

import android.text.TextUtils
import com.booboot.vndbandroid.BuildConfig
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.di.Schedulers
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.*
import com.booboot.vndbandroid.store.ListRepository
import com.booboot.vndbandroid.ui.Presenter
import com.booboot.vndbandroid.util.type
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.Function3
import javax.inject.Inject

open class LoginPresenter @Inject constructor(
        private val vndbServer: VNDBServer,
        private val schedulers: Schedulers,
        private val vnlistRepository: ListRepository<VNlistItem>,
        private val votelistRepository: ListRepository<VotelistItem>,
        private val wishlistRepository: ListRepository<WishlistItem>
) : Presenter<LoginView>() {
    fun login() {
        val vnlistIds = vndbServer.get<VNlistItem>("vnlist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true), type())
        val votelistIds = vndbServer.get<VotelistItem>("votelist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true, socketIndex = 1), type())
        val wishlistIds = vndbServer.get<WishlistItem>("wishlist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true, socketIndex = 2), type())

        val observable = VNDBServer.closeAll()
                .observeOn(schedulers.ui())
                .doOnSubscribe { view?.showLoading(true) }
                .observeOn(schedulers.io())
                .andThen(Single.zip(vnlistIds, votelistIds, wishlistIds,
                        Function3<Results<VNlistItem>, Results<VotelistItem>, Results<WishlistItem>, AccountItems> { vni, vti, wsi ->
                            AccountItems(vni.items, vti.items, wsi.items)
                        }))
                .observeOn(schedulers.io())
                .flatMapMaybe<Results<VN>> { items: AccountItems ->
                    val allIds = items.vnlist.map { it.vn }
                            .union(items.votelist.map { it.vn })
                            .union(items.wishlist.map { it.vn })

                    val newIds = allIds.minus(vnlistRepository.getItems().blockingGet().map { it.vn })
                            .minus(votelistRepository.getItems().blockingGet().map { it.vn })
                            .minus(wishlistRepository.getItems().blockingGet().map { it.vn })

                    vnlistRepository.setItems(items.vnlist)
                    votelistRepository.setItems(items.votelist)
                    wishlistRepository.setItems(items.wishlist)

                    when {
                        allIds.isEmpty() -> Maybe.just(Results()) // empty account
                        newIds.isNotEmpty() -> { // should send get vn
                            val mergedIdsString = TextUtils.join(",", newIds)
                            val numberOfPages = Math.ceil(newIds.size * 1.0 / 25).toInt()

                            vndbServer.get<VN>("vn", "basic,details", "(id = [$mergedIdsString])",
                                    Options(fetchAllPages = true, numberOfPages = numberOfPages), type()).toMaybe()
                        }
                        else -> Maybe.empty()
                    }
                }
                .observeOn(schedulers.ui())
                .doFinally { view?.showLoading(false) }
                .subscribe(::onNext, ::onError)
        composite.add(observable)
    }

    private fun onNext(result: Results<VN>) {
        Preferences.loggedIn = true
        view?.showResult(result)
    }

    private fun onError(throwable: Throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace()
        view?.showError(throwable.localizedMessage)
    }
}