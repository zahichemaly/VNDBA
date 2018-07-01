package com.booboot.vndbandroid.ui.hometabs

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import javax.inject.Inject

class HomeTabsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    val titlesData: MutableLiveData<List<String>> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun getTabTitles(type: Int) {
        accountRepository.getItems().subscribe({ it ->
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
}