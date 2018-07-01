package com.booboot.vndbandroid.ui.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import android.text.TextUtils
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.extensions.completableTransaction
import com.booboot.vndbandroid.model.vndb.*
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.repository.VnlistRepository
import com.booboot.vndbandroid.repository.VotelistRepository
import com.booboot.vndbandroid.repository.WishlistRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.booboot.vndbandroid.util.type
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vndbServer: VNDBServer
    @Inject lateinit var vnlistRepository: VnlistRepository
    @Inject lateinit var votelistRepository: VotelistRepository
    @Inject lateinit var wishlistRepository: WishlistRepository
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var db: DB
    val vnData: MutableLiveData<Results<VN>> = MutableLiveData()

    private lateinit var items: AccountItems
    private var vns: Results<VN>? = null

    init {
        (application as App).appComponent.inject(this)
    }

    fun login() {
        if (disposables.contains(DISPOSABLE_LOGIN)) return

        val vnlistIds = vndbServer.get<Vnlist>("vnlist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true), type())
        val votelistIds = vndbServer.get<Votelist>("votelist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true, socketIndex = 1), type())
        val wishlistIds = vndbServer.get<Wishlist>("wishlist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true, socketIndex = 2), type())

        disposables[DISPOSABLE_LOGIN] = VNDBServer.closeAll()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingData.value = true }
                .observeOn(Schedulers.io())
                .andThen(Single.zip(vnlistIds, votelistIds, wishlistIds,
                        Function3<Results<Vnlist>, Results<Votelist>, Results<Wishlist>, AccountItems> { vni, vti, wsi ->
                            AccountItems(vni.items, vti.items, wsi.items)
                        }))
                .observeOn(Schedulers.io())
                .flatMapMaybe<Results<VN>> { _items: AccountItems ->
                    items = _items

                    val allIds = _items.vnlist.map { it.vn }
                            .union(_items.votelist.map { it.vn })
                            .union(_items.wishlist.map { it.vn })

                    val newIds = allIds.minus(vnlistRepository.getItems().blockingGet().map { it.vn })
                            .minus(votelistRepository.getItems().blockingGet().map { it.vn })
                            .minus(wishlistRepository.getItems().blockingGet().map { it.vn })

                    when {
                        allIds.isEmpty() -> Maybe.just(Results()) // empty account
                        newIds.isNotEmpty() -> { // should send get vn
                            val mergedIdsString = TextUtils.join(",", newIds)
                            val numberOfPages = Math.ceil(newIds.size * 1.0 / 25).toInt()

                            vndbServer.get<VN>("vn", "basic,details,stats", "(id = [$mergedIdsString])",
                                    Options(fetchAllPages = true, numberOfPages = numberOfPages), type()).toMaybe()
                        }
                        else -> Maybe.empty() // nothing new: skipping DB update with an empty result
                    }
                }
                .observeOn(Schedulers.io())
                .flatMapCompletable { _vns ->
                    vns = _vns
                    db.completableTransaction(
                            vnlistRepository.setItems(items.vnlist),
                            votelistRepository.setItems(items.votelist),
                            wishlistRepository.setItems(items.wishlist),
                            vnRepository.setItems(_vns.items)
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    loadingData.value = false
                    disposables.remove(DISPOSABLE_LOGIN)
                }
                .subscribe(::onNext, ::onError)
    }

    private fun onNext() {
        Preferences.loggedIn = true
        vnData.value = vns
    }

    companion object {
        private const val DISPOSABLE_LOGIN = "DISPOSABLE_LOGIN"
    }
}