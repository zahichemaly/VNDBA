package com.booboot.vndbandroid.ui.hometabs

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndb.Label
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.HomeTab
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SORT_LENGTH
import com.booboot.vndbandroid.model.vndbandroid.SORT_POPULARITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_PRIORITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_RATING
import com.booboot.vndbandroid.model.vndbandroid.SORT_RELEASE_DATE
import com.booboot.vndbandroid.model.vndbandroid.SORT_STATUS
import com.booboot.vndbandroid.model.vndbandroid.SORT_TITLE
import com.booboot.vndbandroid.model.vndbandroid.SORT_VOTE
import com.booboot.vndbandroid.model.vndbandroid.SortOptions
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

    fun getTabTitles(force: Boolean = true) = coroutine(DISPOSABLE_TAB_TITLES, !force && vnlistData.value != null) {
        val accountItems = accountRepository.getItems()
        val tabs = listOf(
            HomeTab(Label.PLAYING.id, "Playing (" + accountItems.getLabelCount(Label.PLAYING.id) + ")"),
            HomeTab(Label.FINISHED.id, "Finished (" + accountItems.getLabelCount(Label.FINISHED.id) + ")"),
            HomeTab(Label.STALLED.id, "Stalled (" + accountItems.getLabelCount(Label.STALLED.id) + ")"),
            HomeTab(Label.DROPPED.id, "Dropped (" + accountItems.getLabelCount(Label.DROPPED.id) + ")"),
            HomeTab(Label.UNKNOWN.id, "Unknown (" + accountItems.getLabelCount(Label.UNKNOWN.id) + ")")
        )

        /* Filtering and sorting account items for each tab */
        val tabItems = mutableMapOf<Long, AccountItems>()
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
                    SORT_STATUS -> vn.title // TODO reimplement it properly
                    SORT_VOTE -> vn.title // TODO reimplement it properly
                    SORT_PRIORITY -> vn.title // TODO reimplement it properly
                    else -> id
                }
            }

            val filteredUserList = tabAccountItems.userList.filterValues { tab.value in it.labelIds() }
            tabAccountItems.vns = tabAccountItems.vns
                .filterKeys { vnId -> vnId in filteredUserList }
                .toList()
                .sortedWith(if (Preferences.reverseSort) compareByDescending(sorter) else compareBy(sorter))
                .toMap()

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