package com.booboot.vndbandroid.ui.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.BuildConfig
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.di.Schedulers
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.*
import com.booboot.vndbandroid.store.ListRepository
import com.booboot.vndbandroid.util.type
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import javax.inject.Inject

class LoginViewModel constructor(application: Application) : AndroidViewModel(application) {
    @Inject lateinit var vndbServer: VNDBServer
    @Inject lateinit var schedulers: Schedulers
    @Inject lateinit var vnlistRepository: ListRepository<VNlistItem>
    @Inject lateinit var votelistRepository: ListRepository<VotelistItem>
    @Inject lateinit var wishlistRepository: ListRepository<WishlistItem>
    val vnData: MutableLiveData<Results<VN>> = MutableLiveData()
    val loadingData: MutableLiveData<Boolean> = MutableLiveData()
    val errorData: MutableLiveData<String> = MutableLiveData()
    private val disposables: MutableMap<String, Disposable> = mutableMapOf()

    init {
        (application as App).appComponent.inject(this)
    }

    fun login() {
        if (disposables.contains(DISPOSABLE_LOGIN)) return

        val vnlistIds = vndbServer.get<VNlistItem>("vnlist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true), type())
        val votelistIds = vndbServer.get<VotelistItem>("votelist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true, socketIndex = 1), type())
        val wishlistIds = vndbServer.get<WishlistItem>("wishlist", "basic", "(uid = 0)",
                Options(results = 100, fetchAllPages = true, socketIndex = 2), type())

        disposables[DISPOSABLE_LOGIN] = VNDBServer.closeAll()
                .observeOn(schedulers.ui())
                .doOnSubscribe { loadingData.value = true }
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
                .doFinally {
                    loadingData.value = false
                    disposables.remove(DISPOSABLE_LOGIN)
                }
                .subscribe(::onNext, ::onError)
    }

    private fun onNext(result: Results<VN>) {
        Preferences.loggedIn = true
        vnData.value = result
    }

    private fun onError(throwable: Throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace()
        errorData.value = throwable.localizedMessage
        errorData.value = null
    }

    companion object {
        private const val DISPOSABLE_LOGIN = "DISPOSABLE_LOGIN"
    }
}