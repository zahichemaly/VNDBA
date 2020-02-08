package com.booboot.vndbandroid.ui.vnlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.diff.VNDiffCallback
import com.booboot.vndbandroid.extensions.lowerCase
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
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
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import javax.inject.Inject

class VNListViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    val vnlistData = MutableLiveData<VnlistData>()
    val scrollToTopData = MutableLiveData<Boolean>()

    var filter = ""

    init {
        (application as App).appComponent.inject(this)
    }

    fun getVns(
        _filter: String = filter,
        @SortOptions _sort: Int = Preferences.sort,
        _reverseSort: Boolean = Preferences.reverseSort,
        scrollToTop: Boolean = true
    ) {
        filter = _filter.lowerCase()
        Preferences.sort = _sort
        Preferences.reverseSort = _reverseSort

        coroutine(JOB_GET_VNS) {
            /* #122 : to make DiffUtil work in the Adapter, the items must be deep copied here so the contents can be identified as different when changed from inside the app */
            val accountItems = accountRepository.getItems().deepCopy()

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

            accountItems.vns = accountItems.vns
                .filterValues { vn -> vn.title.trim().lowerCase().contains(filter) }
                .toList()
                .sortedWith(if (Preferences.reverseSort) compareByDescending(sorter) else compareBy(sorter))
                .toMap()

            vnlistData += VnlistData(accountItems).apply {
                val diffCallback = VNDiffCallback(vnlistData.value?.items ?: AccountItems(), items)
                diffResult = DiffUtil.calculateDiff(diffCallback)
            }
            scrollToTopData += scrollToTop
        }
    }

    companion object {
        private const val JOB_GET_VNS = "JOB_GET_VNS"
    }
}

data class VnlistData(
    val items: AccountItems = AccountItems(),
    var diffResult: DiffUtil.DiffResult? = null
)