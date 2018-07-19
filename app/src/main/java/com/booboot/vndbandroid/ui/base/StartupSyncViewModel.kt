package com.booboot.vndbandroid.ui.base

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.extensions.completableTransaction
import com.booboot.vndbandroid.extensions.leaveIfEmpty
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.repository.VnlistRepository
import com.booboot.vndbandroid.repository.VotelistRepository
import com.booboot.vndbandroid.repository.WishlistRepository
import com.booboot.vndbandroid.util.type
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

abstract class StartupSyncViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vndbServer: VNDBServer
    @Inject lateinit var vnlistRepository: VnlistRepository
    @Inject lateinit var votelistRepository: VotelistRepository
    @Inject lateinit var wishlistRepository: WishlistRepository
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var db: DB

    private lateinit var items: AccountItems
    val accountData: MutableLiveData<AccountItems> = MutableLiveData()

    protected fun startupSyncCompletable(): Single<AccountItems> {
        val vnlistIds = vndbServer.get<Vnlist>("vnlist", "basic", "(uid = 0)",
            Options(results = 100, fetchAllPages = true), type())
        val votelistIds = vndbServer.get<Votelist>("votelist", "basic", "(uid = 0)",
            Options(results = 100, fetchAllPages = true, socketIndex = 1), type())
        val wishlistIds = vndbServer.get<Wishlist>("wishlist", "basic", "(uid = 0)",
            Options(results = 100, fetchAllPages = true, socketIndex = 2), type())

        // TODO add the auto-update of Tags and Traits as another thread inside the zip()
        return Single.zip(vnlistIds, votelistIds, wishlistIds,
            Function3<Results<Vnlist>, Results<Votelist>, Results<Wishlist>, AccountItems> { vni, vti, wsi ->
                AccountItems(vni.items, vti.items, wsi.items)
            })
            .observeOn(Schedulers.io())
            .flatMapMaybe<Results<VN>> { _items: AccountItems ->
                items = _items

                val allIds = _items.vnlist.map { it.vn }
                    .union(_items.votelist.map { it.vn })
                    .union(_items.wishlist.map { it.vn })

                val oldVnlist = vnlistRepository.getItems().blockingGet()
                val oldVotelist = votelistRepository.getItems().blockingGet()
                val oldWishlist = wishlistRepository.getItems().blockingGet()

                val newIds = allIds.minus(oldVnlist.map { it.vn })
                    .minus(oldVotelist.map { it.vn })
                    .minus(oldWishlist.map { it.vn })

                val haveListsChanged = items.vnlist != oldVnlist ||
                    items.votelist != oldVotelist ||
                    items.wishlist != oldWishlist

                when {
                    allIds.isEmpty() -> Maybe.just(Results()) // empty account
                    newIds.isNotEmpty() -> { // should send get vn
                        val mergedIdsString = TextUtils.join(",", newIds)
                        val numberOfPages = Math.ceil(newIds.size * 1.0 / 25).toInt()

                        vndbServer.get<VN>("vn", "basic,details,stats", "(id = [$mergedIdsString])",
                            Options(fetchAllPages = true, numberOfPages = numberOfPages), type()).toMaybe()
                    }
                    haveListsChanged -> Maybe.just(Results()) // no new VNs but status of existing VNs have changed
                    else -> Maybe.empty() // nothing new: skipping DB update with an empty result
                }
            }
            .leaveIfEmpty()
            .observeOn(Schedulers.io())
            .flatMapCompletable {
                db.completableTransaction(
                    vnlistRepository.setItems(items.vnlist),
                    votelistRepository.setItems(items.votelist),
                    wishlistRepository.setItems(items.wishlist),
                    vnRepository.setItems(it.items)
                )
                // TODO DB startup clean
            }
            .andThen(accountRepository.getItems())
    }
}