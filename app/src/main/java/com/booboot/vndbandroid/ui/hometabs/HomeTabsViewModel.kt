package com.booboot.vndbandroid.ui.hometabs

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.HomeTab
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.SORT_LENGTH
import com.booboot.vndbandroid.model.vndbandroid.SORT_POPULARITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_PRIORITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_RATING
import com.booboot.vndbandroid.model.vndbandroid.SORT_RELEASE_DATE
import com.booboot.vndbandroid.model.vndbandroid.SORT_STATUS
import com.booboot.vndbandroid.model.vndbandroid.SORT_TITLE
import com.booboot.vndbandroid.model.vndbandroid.SORT_VOTE
import com.booboot.vndbandroid.model.vndbandroid.SortOptions
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.model.vndbandroid.VnlistData
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

class HomeTabsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    val vnlistData: MutableLiveData<VnlistData> = MutableLiveData()
    val sortData: MutableLiveData<Preferences> = MutableLiveData()

    var currentPage = -1
    var sortBottomSheetState = BottomSheetBehavior.STATE_HIDDEN

    init {
        (application as App).appComponent.inject(this)
    }

    fun getTabTitles(listType: Int, force: Boolean = true) = coroutine(DISPOSABLE_TAB_TITLES, !force && vnlistData.value != null) {
        val accountItems = accountRepository.getItems(this).await()
        val tabs = when (listType) {
            HomeTabsFragment.VNLIST -> {
                val statusCount = accountItems.getStatusCount()
                listOf(
                    HomeTab(Status.PLAYING, "Playing (" + statusCount[Status.PLAYING] + ")"),
                    HomeTab(Status.FINISHED, "Finished (" + statusCount[Status.FINISHED] + ")"),
                    HomeTab(Status.STALLED, "Stalled (" + statusCount[Status.STALLED] + ")"),
                    HomeTab(Status.DROPPED, "Dropped (" + statusCount[Status.DROPPED] + ")"),
                    HomeTab(Status.UNKNOWN, "Unknown (" + statusCount[Status.UNKNOWN] + ")")
                )
            }

            HomeTabsFragment.VOTELIST -> {
                val voteCount = accountItems.getVoteCount()
                listOf(
                    HomeTab(10, "10 - 9 (" + voteCount[0] + ")"),
                    HomeTab(8, "8 - 7 (" + voteCount[1] + ")"),
                    HomeTab(6, "6 - 5 (" + voteCount[2] + ")"),
                    HomeTab(4, "4 - 3 (" + voteCount[3] + ")"),
                    HomeTab(2, "2 - 1 (" + voteCount[4] + ")")
                )
            }

            HomeTabsFragment.WISHLIST -> {
                val wishCount = accountItems.getWishCount()
                listOf(
                    HomeTab(Priority.HIGH, "High (" + wishCount[Priority.HIGH] + ")"),
                    HomeTab(Priority.MEDIUM, "Medium (" + wishCount[Priority.MEDIUM] + ")"),
                    HomeTab(Priority.LOW, "Low (" + wishCount[Priority.LOW] + ")"),
                    HomeTab(Priority.BLACKLIST, "Blacklist (" + wishCount[Priority.BLACKLIST] + ")")
                )
            }
            else -> emptyList()
        }

        /* Filtering and sorting account items for each tab */
        val tabItems = mutableMapOf<Int, AccountItems>()
        tabs.forEach { tab ->
            /* Each tab must work on a different copy of account items */
            /* #122 : to make DiffUtil work in the Adapter, the items must be deep copied here so the contents can be identified as different when changed from inside the app */
            val tabAccountItems = accountItems.deepCopy()

            val sorter: (Pair<Long, VN>) -> Comparable<*>? = { (id, vn) ->
                when (Preferences.sort) {
                    SORT_TITLE -> vn.title
                    SORT_RELEASE_DATE -> vn.released
                    SORT_LENGTH -> vn.length
                    SORT_POPULARITY -> vn.popularity
                    SORT_RATING -> vn.rating
                    SORT_STATUS -> tabAccountItems.vnlist[id]?.status
                    SORT_VOTE -> tabAccountItems.votelist[id]?.vote
                    SORT_PRIORITY -> tabAccountItems.wishlist[id]?.priority
                    else -> id
                }
            }

            tabAccountItems.vns = tabAccountItems.vns.filterKeys { vnId ->
                when (listType) {
                    HomeTabsFragment.VNLIST -> vnId in tabAccountItems.vnlist.filterValues { it.status == tab.value }
                    HomeTabsFragment.VOTELIST -> vnId in tabAccountItems.votelist.filterValues { it.vote / 10 == tab.value || it.vote / 10 == tab.value - 1 }
                    HomeTabsFragment.WISHLIST -> vnId in tabAccountItems.wishlist.filterValues { it.priority == tab.value }
                    else -> true
                }
            }.toList().sortedWith(if (Preferences.reverseSort) compareByDescending(sorter) else compareBy(sorter)).toMap()

            tabItems[tab.value] = tabAccountItems
        }

        vnlistData += VnlistData(tabs, tabItems)
    }

    fun setSort(@SortOptions sort: Int) {
        Preferences.sort = sort
        sortData.value = Preferences
    }

    fun reverseSort() {
        Preferences.reverseSort = !Preferences.reverseSort
        sortData.value = Preferences
    }

    override fun restoreState(state: Bundle?) {
        super.restoreState(state)
        state ?: return
        currentPage = state.getInt(CURRENT_PAGE)
        sortBottomSheetState = state.getInt(SORT_BOTTOM_SHEET_STATE)
    }

    override fun saveState(state: Bundle) {
        super.saveState(state)
        state.putInt(CURRENT_PAGE, currentPage)
        state.putInt(SORT_BOTTOM_SHEET_STATE, sortBottomSheetState)
    }

    companion object {
        private const val DISPOSABLE_TAB_TITLES = "DISPOSABLE_TAB_TITLES"
        private const val CURRENT_PAGE = "CURRENT_PAGE"
        private const val SORT_BOTTOM_SHEET_STATE = "SORT_BOTTOM_SHEET_STATE"
    }
}