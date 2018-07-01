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
    val vnData: MutableLiveData<AccountItems> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun getVns(listType: Int, tabValue: Int) {
        accountRepository.getItems().subscribe({ cache ->
            cache.vns = cache.vns.filter {
                when (listType) {
                    VNLIST -> it.id in cache.vnlist.filter { it.status == tabValue }.map { it.vn }

                    VOTELIST -> it.id in cache.votelist.filter { it.vote / 10 == tabValue || it.vote / 10 == tabValue - 1 }.map { it.vn }

                    WISHLIST -> it.id in cache.wishlist.filter { it.priority == tabValue }.map { it.vn }

                    else -> true
                }
            }

            vnData.value = cache
        }, ::onError)
    }
}