package com.booboot.vndbandroid.ui.vnlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SORT_LENGTH
import com.booboot.vndbandroid.model.vndbandroid.SORT_POPULARITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_PRIORITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_RATING
import com.booboot.vndbandroid.model.vndbandroid.SORT_RELEASE_DATE
import com.booboot.vndbandroid.model.vndbandroid.SORT_STATUS
import com.booboot.vndbandroid.model.vndbandroid.SORT_TITLE
import com.booboot.vndbandroid.model.vndbandroid.SORT_VOTE
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.VNLIST
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.VOTELIST
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.WISHLIST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VNListViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    val accountData: MutableLiveData<AccountItems> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun getVns(listType: Int, tabValue: Int, force: Boolean = true) = coroutine(DISPOSABLE_GET_VN, !force && accountData.value != null) {
        val accountItems = accountRepository.getItems(this).await()
        accountData.value = withContext(Dispatchers.Default) {
            val vnlist = accountItems.vnlist.filterValues { it.status == tabValue }
            val votelist = accountItems.votelist.filterValues { it.vote / 10 == tabValue || it.vote / 10 == tabValue - 1 }
            val wishlist = accountItems.wishlist.filterValues { it.priority == tabValue }

            val sorter: (Pair<Long, VN>) -> Comparable<*>? = { (id, vn) ->
                when (Preferences.sort) {
                    SORT_TITLE -> vn.title
                    SORT_RELEASE_DATE -> vn.released
                    SORT_LENGTH -> vn.length
                    SORT_POPULARITY -> vn.popularity
                    SORT_RATING -> vn.rating
                    SORT_STATUS -> accountItems.vnlist[id]?.status
                    SORT_VOTE -> accountItems.votelist[id]?.vote
                    SORT_PRIORITY -> accountItems.wishlist[id]?.priority
                    else -> id
                }
            }

            accountItems.vns = accountItems.vns.filterKeys {
                when (listType) {
                    VNLIST -> it in vnlist
                    VOTELIST -> it in votelist
                    WISHLIST -> it in wishlist
                    else -> true
                }
            }.toList().sortedWith(if (Preferences.reverseSort) compareByDescending(sorter) else compareBy(sorter)).toMap()

            /* #122 : to make DiffUtil work in the Adapter, the items must be deep copied here so the contents can be identified as different when changed from inside the app */
            accountItems.deepCopy()
        }
    }

    companion object {
        private const val DISPOSABLE_GET_VN = "DISPOSABLE_GET_VN"
    }
}