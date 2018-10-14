package com.booboot.vndbandroid.ui.vndetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.minus
import com.booboot.vndbandroid.extensions.plus
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.repository.VnlistRepository
import com.booboot.vndbandroid.repository.VotelistRepository
import com.booboot.vndbandroid.repository.WishlistRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VNDetailsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var vnlistRepository: VnlistRepository
    @Inject lateinit var votelistRepository: VotelistRepository
    @Inject lateinit var wishlistRepository: WishlistRepository

    val vnData: MutableLiveData<VN> = MutableLiveData()
    val accountData: MutableLiveData<AccountItems> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(vnId: Long, force: Boolean = true) {
        if (!force && vnData.value != null) return
        if (disposables.contains(DISPOSABLE_VN)) return

        disposables[DISPOSABLE_VN] = vnRepository.getItem(vnId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.plus() }
            .doFinally {
                loadingData.minus()
                disposables.remove(DISPOSABLE_VN)
            }
            .subscribe({
                vnData.value = it
            }, ::onError)
    }

    fun loadAccount(force: Boolean = true) {
        if (!force && accountData.value != null) return
        if (disposables.contains(DISPOSABLE_ACCOUNT)) return

        disposables[DISPOSABLE_ACCOUNT] = accountRepository.getItems()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { disposables.remove(DISPOSABLE_ACCOUNT) }
            .subscribe({ accountData.value = it }, ::onError)
    }

    fun setVnlist(vnlist: Vnlist) {
        if (accountData.value?.vnlist?.get(vnData.value?.id) == vnlist) return
        vnlistRepository.setItem(vnlist).setAndSubscribe()
    }

    fun setVotelist(votelist: Votelist) {
        if (accountData.value?.votelist?.get(vnData.value?.id) == votelist) return
        votelistRepository.setItem(votelist).setAndSubscribe()
    }

    fun removeVnlist(vnlist: Vnlist) {
        if (accountData.value?.vnlist?.get(vnData.value?.id) == null) return
        vnlistRepository.deleteItem(vnlist).setAndSubscribe()
    }

    fun removeVotelist(votelist: Votelist) {
        if (accountData.value?.votelist?.get(vnData.value?.id) == null) return
        votelistRepository.deleteItem(votelist).setAndSubscribe()
    }

    private fun Completable.setAndSubscribe() = subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .andThen(accountRepository.getItems())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ accountData.value = it }, ::onError)

    companion object {
        private const val DISPOSABLE_VN = "DISPOSABLE_VN"
        private const val DISPOSABLE_ACCOUNT = "DISPOSABLE_ACCOUNT"
    }
}