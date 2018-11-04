package com.booboot.vndbandroid.ui.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.extensions.completableTransaction
import com.booboot.vndbandroid.extensions.leaveIfEmpty
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndb.Trait
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SyncData
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.CachePolicy
import com.booboot.vndbandroid.repository.TagsRepository
import com.booboot.vndbandroid.repository.TraitsRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.repository.VnlistRepository
import com.booboot.vndbandroid.repository.VotelistRepository
import com.booboot.vndbandroid.repository.WishlistRepository
import com.booboot.vndbandroid.util.EmptyMaybeException
import io.objectbox.BoxStore
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
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
    @Inject lateinit var tagsRepository: TagsRepository
    @Inject lateinit var traitsRepository: TraitsRepository
    @Inject lateinit var boxStore: BoxStore

    private lateinit var items: AccountItems
    val syncAccountData: MutableLiveData<AccountItems> = MutableLiveData()
    val syncData: MutableLiveData<SyncData> = MutableLiveData()

    protected fun startupSyncSingle(doOnAccountSuccess: (AccountItems) -> Unit = {}): Single<SyncData> {
        var pendingError: Throwable? = null
        val accountSingle = Single.zip(
            vnlistRepository.getItems(CachePolicy(false)),
            votelistRepository.getItems(CachePolicy(false)),
            wishlistRepository.getItems(CachePolicy(false)),
            Function3<Map<Long, Vnlist>, Map<Long, Votelist>, Map<Long, Wishlist>, AccountItems> { vni, vti, wsi ->
                AccountItems(vni, vti, wsi)
            })
            .observeOn(Schedulers.io())
            .flatMapMaybe<Map<Long, VN>> { _items: AccountItems ->
                items = _items

                val allIds = _items.vnlist.keys
                    .union(_items.votelist.keys)
                    .union(_items.wishlist.keys)

                val oldItems = Single.zip(
                    vnlistRepository.getItems(CachePolicy(cacheOnly = true)),
                    votelistRepository.getItems(CachePolicy(cacheOnly = true)),
                    wishlistRepository.getItems(CachePolicy(cacheOnly = true)),
                    Function3<Map<Long, Vnlist>, Map<Long, Votelist>, Map<Long, Wishlist>, AccountItems> { vni, vti, wsi ->
                        AccountItems(vni, vti, wsi)
                    }).blockingGet()

                val newIds = allIds.minus(oldItems.vnlist.keys)
                    .minus(oldItems.votelist.keys)
                    .minus(oldItems.wishlist.keys)

                val haveListsChanged = items.vnlist != oldItems.vnlist ||
                    items.votelist != oldItems.votelist ||
                    items.wishlist != oldItems.wishlist

                when {
                    allIds.isEmpty() -> Maybe.just(emptyMap()) // empty account
                    newIds.isNotEmpty() -> { // should send get vn
                        vnRepository.getItems(newIds, FLAGS_DETAILS, CachePolicy(false)).toMaybe()
                    }
                    haveListsChanged -> Maybe.just(emptyMap()) // no new VNs but status of existing VNs have changed
                    else -> Maybe.empty() // nothing new: skipping DB update with an empty result
                }
            }
            .leaveIfEmpty()
            .observeOn(Schedulers.io())
            .flatMapCompletable {
                boxStore.completableTransaction(
                    vnlistRepository.setItems(items.vnlist),
                    votelistRepository.setItems(items.votelist),
                    wishlistRepository.setItems(items.wishlist),
                    vnRepository.setItems(it)
                )
                // TODO DB startup clean (in a new flatmap, must make sure the above transaction has completed before)
            }
            .andThen(accountRepository.getItems())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                /* This Single is going to be zipped with other independent and longer Singles: sending a UI update now to be as fast as possible */
                /* During login, don't use this LiveData because we always need account data + tags + traits before proceeding! Home only needs account data though so can use this LiveData. */
                syncAccountData.value = it
                doOnAccountSuccess(it)
            }
            .onErrorResumeNext {
                /* If leaveIfEmpty(), sync should continue anyway and still update tags and traits, without going in the above doOnSuccess, hence returning an empty result only now */
                if (it is EmptyMaybeException) accountRepository.getItems()
                else Single.error(it)
            }

        val tagsSingle = tagsRepository
            .getItems(CachePolicy(true))
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext {
                pendingError = it
                Single.just(emptyMap())
            }
        val traitsSingle = traitsRepository
            .getItems(CachePolicy(true))
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext {
                pendingError = it
                Single.just(emptyMap())
            }

        return Single.zip(accountSingle, tagsSingle, traitsSingle, Function3<AccountItems, Map<Long, Tag>, Map<Long, Trait>, SyncData> { accountItems, tags, traits ->
            pendingError?.let { throw it }
            SyncData(accountItems, tags, traits)
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Preferences.loggedIn = true
                syncData.value = it
            }
    }
}