package com.booboot.vndbandroid.ui.hometabs

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.SortOptions
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class HomeTabsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    val titlesData: MutableLiveData<List<String>> = MutableLiveData()
    val sortData: MutableLiveData<Preferences> = MutableLiveData()

    var currentPage = -1
    var sortBottomSheetState = BottomSheetBehavior.STATE_HIDDEN

    init {
        (application as App).appComponent.inject(this)
    }

    fun getTabTitles(type: Int, force: Boolean = true) {
        if (!force && titlesData.value != null) return
        if (disposables.contains(DISPOSABLE_TAB_TITLES)) return

        disposables[DISPOSABLE_TAB_TITLES] = accountRepository.getItems()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { disposables.remove(DISPOSABLE_TAB_TITLES) }
            .subscribe({
                val titles = when (type) {
                    HomeTabsFragment.VNLIST -> {
                        val statusCount = it.getStatusCount()
                        listOf("Playing (" + statusCount[Status.PLAYING] + ")",
                            "Finished (" + statusCount[Status.FINISHED] + ")",
                            "Stalled (" + statusCount[Status.STALLED] + ")",
                            "Dropped (" + statusCount[Status.DROPPED] + ")",
                            "Unknown (" + statusCount[Status.UNKNOWN] + ")"
                        )
                    }

                    HomeTabsFragment.VOTELIST -> {
                        val voteCount = it.getVoteCount()
                        listOf("10 - 9 (" + voteCount[0] + ")",
                            "8 - 7 (" + voteCount[1] + ")",
                            "6 - 5 (" + voteCount[2] + ")",
                            "4 - 3 (" + voteCount[3] + ")",
                            "2 - 1 (" + voteCount[4] + ")"
                        )
                    }

                    HomeTabsFragment.WISHLIST -> {
                        val wishCount = it.getWishCount()
                        listOf("High (" + wishCount[Priority.HIGH] + ")",
                            "Medium (" + wishCount[Priority.MEDIUM] + ")",
                            "Low (" + wishCount[Priority.LOW] + ")",
                            "Blacklist (" + wishCount[Priority.BLACKLIST] + ")"
                        )
                    }
                    else -> emptyList()
                }

                titlesData.value = titles
            }, ::onError)
    }

    fun setSort(@SortOptions sort: Int) {
        Preferences.sort = sort
        sortData.value = Preferences
    }

    fun reverseSort() {
        Preferences.reverseSort = !Preferences.reverseSort
        sortData.value = Preferences
    }

    companion object {
        private const val DISPOSABLE_TAB_TITLES = "DISPOSABLE_TAB_TITLES"
    }
}