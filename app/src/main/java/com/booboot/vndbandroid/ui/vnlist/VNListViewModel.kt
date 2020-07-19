package com.booboot.vndbandroid.ui.vnlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.diff.VNDiffCallback
import com.booboot.vndbandroid.extensions.addOrCreate
import com.booboot.vndbandroid.extensions.lowerCase
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.model.vndb.Label.Companion.NO_LABELS
import com.booboot.vndbandroid.model.vndb.Label.Companion.NO_VOTE
import com.booboot.vndbandroid.model.vndb.Label.Companion.STATUSES
import com.booboot.vndbandroid.model.vndb.Label.Companion.VOTED
import com.booboot.vndbandroid.model.vndb.Label.Companion.VOTELISTS
import com.booboot.vndbandroid.model.vndb.Label.Companion.VOTES
import com.booboot.vndbandroid.model.vndb.Label.Companion.WISHLISTS
import com.booboot.vndbandroid.model.vndb.UserLabel
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.FilterData
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
import com.booboot.vndbandroid.repository.UserLabelsRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.booboot.vndbandroid.ui.filters.FilterSubtitleItem
import com.booboot.vndbandroid.ui.filters.FilterTitleItem
import com.booboot.vndbandroid.ui.filters.LabelItem
import com.booboot.vndbandroid.ui.filters.VoteItem
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.coroutines.async
import javax.inject.Inject

class VNListViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var accountRepository: AccountRepository
    @Inject lateinit var userLabelsRepository: UserLabelsRepository

    val vnlistData = MutableLiveData<VnlistData>()
    val filterData = MutableLiveData<FilterData>()
    val scrollToTopData = MutableLiveData<Boolean>()

    var filter = ""
    val sortGroup: ExpandableGroup
    val sortSection = Section()
    val filterGroup: ExpandableGroup
    val filterSection = Section()

    init {
        (application as App).appComponent.inject(this)
        sortGroup = ExpandableGroup(FilterTitleItem(-1, R.string.sort_vns_by, R.drawable.ic_sort_24dp), true).apply {
            add(sortSection)
        }
        filterGroup = ExpandableGroup(FilterTitleItem(-2, R.string.filter_vns_by, R.drawable.ic_filter_list_24dp), true).apply {
            add(filterSection)
        }
    }

    fun getVns(
        _filter: String = filter,
        @SortOptions sort: Long = Preferences.sort,
        selectedLabelId: Long = NO_LABELS,
        reverseSort: Boolean = Preferences.reverseSort,
        scrollToTop: Boolean = true
    ) {
        filter = _filter.lowerCase()
        Preferences.sort = sort
        Preferences.reverseSort = reverseSort

        coroutine(JOB_GET_VNS) {
            val selectedFiltersJob = async { userLabelsRepository.selectAsFilter(selectedLabelId) }

            /* #122 : to make DiffUtil work in the Adapter, the items must be deep copied here so the contents can be identified as different when changed from inside the app */
            val accountItems = accountRepository.getItems().deepCopy()
            val selectedFilters = selectedFiltersJob.await()

            val sortJob = async {
                val sorter: Comparator<(Pair<Long, VN>)> = when (Preferences.sort) {
                    SORT_TITLE -> compare(nullsLast()) { (_, vn) -> vn.title.trimStart().lowerCase() }
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
                    .filterValues { vn -> vn.filterTitle(filter) && accountItems.filterLabel(vn.id, selectedFilters) }
                    .toList()
                    .sortedWith(sorter)
                    .toMap()
            }

            val categorizedLabels = linkedMapOf<FilterSubtitleItem, MutableList<Item>>()
            val addLabel = { group: FilterSubtitleItem, userLabel: UserLabel, selected: Boolean ->
                val labelItem = when (userLabel.id) {
                    in VOTELISTS -> VoteItem(userLabel, selected).apply { onLabelClicked = ::onLabelClicked }
                    else -> LabelItem(userLabel, selected).apply { onLabelClicked = ::onLabelClicked }
                }
                categorizedLabels.addOrCreate(group, labelItem)
            }
            val labelsJob = async {
                accountItems.userLabels.values.forEach { userLabel ->
                    val selectedAsFilter = userLabel.id in selectedFilters
                    when (userLabel.id) {
                        in STATUSES -> addLabel(FilterSubtitleItem(R.drawable.ic_list_48dp, R.string.status), userLabel, selectedAsFilter)
                        in WISHLISTS -> addLabel(FilterSubtitleItem(R.drawable.ic_wishlist, R.string.wishlist), userLabel, selectedAsFilter)
                        VOTED.id -> {
                            val subtitleItem = FilterSubtitleItem(R.drawable.ic_format_list_numbered_48dp, R.string.votes)
                            /* Votes from 1 to 10 */
                            for (vote in VOTES) addLabel(subtitleItem, UserLabel(vote.id, vote.label), vote.id in selectedFilters)
                            addLabel(subtitleItem, UserLabel(userLabel.id, getApplication<App>().getString(R.string.any_vote)), selectedAsFilter)
                            addLabel(subtitleItem, UserLabel(NO_VOTE.id, NO_VOTE.label), NO_VOTE.id in selectedFilters)
                        }
                        else -> addLabel(FilterSubtitleItem(R.drawable.ic_list_48dp, R.string.category_custom_labels), userLabel, selectedAsFilter)
                    }
                }
            }

            sortJob.await()
            labelsJob.await()

            val diffResult = DiffUtil.calculateDiff(VNDiffCallback(vnlistData.value?.items ?: AccountItems(), accountItems))
            vnlistData += VnlistData(accountItems, diffResult)
            filterData += FilterData(sort, reverseSort, categorizedLabels, selectedFilters)
            scrollToTopData += scrollToTop
        }
    }

    fun onLabelClicked(labelId: Long) {
        getVns(selectedLabelId = labelId)
    }

    private fun <T> compare(comparator: Comparator<in T>, sorter: (Pair<Long, VN>) -> T) =
        if (Preferences.reverseSort) compareByDescending(comparator, sorter) else compareBy(comparator, sorter)

    companion object {
        private const val JOB_GET_VNS = "JOB_GET_VNS"
    }
}