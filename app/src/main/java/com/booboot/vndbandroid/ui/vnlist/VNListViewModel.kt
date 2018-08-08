package com.booboot.vndbandroid.ui.vnlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.VNLIST
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.VOTELIST
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.WISHLIST
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VNListViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    val accountData: MutableLiveData<AccountItems> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun getVns(listType: Int, tabValue: Int, force: Boolean = true) {
        if (!force && accountData.value != null) return
        if (disposables.contains(DISPOSABLE_GET_VN)) return

        disposables[DISPOSABLE_GET_VN] = accountRepository.getItems()
            .observeOn(Schedulers.computation())
            .map { cache ->
                val vnlist = cache.vnlist.filterValues { it.status == tabValue }
                val votelist = cache.votelist.filterValues { it.vote / 10 == tabValue || it.vote / 10 == tabValue - 1 }
                val wishlist = cache.wishlist.filterValues { it.priority == tabValue }

                cache.vns = cache.vns.filterKeys {
                    when (listType) {
                        VNLIST -> it in vnlist
                        VOTELIST -> it in votelist
                        WISHLIST -> it in wishlist
                        else -> true
                    }
                }
                cache
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { disposables.remove(DISPOSABLE_GET_VN) }
            .subscribe({ accountData.value = it }, ::onError)
    }

    companion object {
        private const val DISPOSABLE_GET_VN = "DISPOSABLE_GET_VN"
    }
}