package com.booboot.vndbandroid.ui.vnlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.diff.FilterDiffCallback
import com.booboot.vndbandroid.diff.VNDiffCallback
import com.booboot.vndbandroid.extensions.lowerCase
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.extensions.upperCase
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.FilterCheckbox
import com.booboot.vndbandroid.model.vndbandroid.FilterData
import com.booboot.vndbandroid.model.vndbandroid.FilterTitle
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SORT_ID
import com.booboot.vndbandroid.model.vndbandroid.SORT_LENGTH
import com.booboot.vndbandroid.model.vndbandroid.SORT_POPULARITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_PRIORITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_RATING
import com.booboot.vndbandroid.model.vndbandroid.SORT_RELEASE_DATE
import com.booboot.vndbandroid.model.vndbandroid.SORT_STATUS
import com.booboot.vndbandroid.model.vndbandroid.SORT_TITLE
import com.booboot.vndbandroid.model.vndbandroid.SORT_VOTE
import com.booboot.vndbandroid.model.vndbandroid.SortItem
import com.booboot.vndbandroid.model.vndbandroid.SortOptions
import com.booboot.vndbandroid.model.vndbandroid.VnlistData
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import javax.inject.Inject

class VNListViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    val vnlistData = MutableLiveData<VnlistData>()
    val filterData = MutableLiveData<FilterData>()
    val scrollToTopData = MutableLiveData<Boolean>()

    var filter = ""

    init {
        (application as App).appComponent.inject(this)
    }

    fun getVns(
        _filter: String = filter,
        @SortOptions _sort: Long = Preferences.sort,
        _reverseSort: Boolean = Preferences.reverseSort,
        scrollToTop: Boolean = true
    ) {
        filter = _filter.lowerCase()
        Preferences.sort = _sort
        Preferences.reverseSort = _reverseSort

        coroutine(JOB_GET_VNS) {
            /* #122 : to make DiffUtil work in the Adapter, the items must be deep copied here so the contents can be identified as different when changed from inside the app */
            val accountItems = accountRepository.getItems().deepCopy()

            val sorter: Comparator<(Pair<Long, VN>)> = when (Preferences.sort) {
                SORT_TITLE -> compare(nullsLast()) { (_, vn) -> vn.title.trim().upperCase() }
                SORT_RELEASE_DATE -> compare(nullsFirst()) { (_, vn) -> vn.released }
                SORT_LENGTH -> compare(nullsLast()) { (_, vn) -> vn.length }
                SORT_POPULARITY -> compare(nullsLast()) { (_, vn) -> vn.popularity }
                SORT_RATING -> compare(nullsLast()) { (_, vn) -> vn.rating }
                SORT_STATUS -> compare(nullsLast()) { (_, vn) -> accountItems.userList[vn.id]?.firstStatus() }
                SORT_PRIORITY -> compare(nullsLast()) { (_, vn) -> accountItems.userList[vn.id]?.firstWishlist() }
                SORT_VOTE -> compare(nullsFirst()) { (_, vn) -> accountItems.userList[vn.id]?.vote }
                else -> compare(nullsLast()) { (id, _) -> id }
            }

            accountItems.vns = accountItems.vns
                .run {
                    if (filter.isEmpty()) this
                    else filterValues { vn -> vn.title.trim().lowerCase().contains(filter) }
                }
                .toList()
                .sortedWith(sorter)
                .toMap()

            val filters = listOf(
                FilterTitle(-1, R.string.sort_vns_by, R.drawable.ic_filter_list_24dp, 0),
                FilterCheckbox(-2, R.string.reverse_order, _reverseSort).apply {
                    onClicked = { getVns(_reverseSort = !selected) }
                },
                SortItem(SORT_ID, R.string.id, _sort == SORT_ID),
                SortItem(SORT_TITLE, R.string.title, _sort == SORT_TITLE),
                SortItem(SORT_RELEASE_DATE, R.string.release_date, _sort == SORT_RELEASE_DATE),
                SortItem(SORT_LENGTH, R.string.length, _sort == SORT_LENGTH),
                SortItem(SORT_POPULARITY, R.string.popularity, _sort == SORT_POPULARITY),
                SortItem(SORT_RATING, R.string.rating, _sort == SORT_RATING),
                SortItem(SORT_STATUS, R.string.status, _sort == SORT_STATUS),
                SortItem(SORT_VOTE, R.string.vote, _sort == SORT_VOTE),
                SortItem(SORT_PRIORITY, R.string.priority, _sort == SORT_PRIORITY)
            )

            val diffResult = DiffUtil.calculateDiff(VNDiffCallback(vnlistData.value?.items ?: AccountItems(), accountItems))
            val filterDiffResult = DiffUtil.calculateDiff(FilterDiffCallback(filterData.value?.items ?: listOf(), filters))
            vnlistData += VnlistData(accountItems, diffResult)
            filterData += FilterData(filters, filterDiffResult)
            scrollToTopData += scrollToTop
        }
    }

    private fun <T> compare(comparator: Comparator<in T>, sorter: (Pair<Long, VN>) -> T) =
        if (Preferences.reverseSort) compareByDescending(comparator, sorter) else compareBy(comparator, sorter)

    companion object {
        private const val JOB_GET_VNS = "JOB_GET_VNS"
    }
}