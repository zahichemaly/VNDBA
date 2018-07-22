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
            .doFinally { disposables.remove(DISPOSABLE_GET_VN) }
            .subscribe({ cache ->
                cache.vns = cache.vns.filterKeys {
                    when (listType) {
                        VNLIST -> it in cache.vnlist.filterValues { it.status == tabValue }.keys

                        VOTELIST -> it in cache.votelist.filterValues { it.vote / 10 == tabValue || it.vote / 10 == tabValue - 1 }.keys

                        WISHLIST -> it in cache.wishlist.filterValues { it.priority == tabValue }.keys

                        else -> true
                    }
                }

                accountData.value = cache
            }, ::onError)
    }

    companion object {
        private const val DISPOSABLE_GET_VN = "DISPOSABLE_GET_VN"
    }
}