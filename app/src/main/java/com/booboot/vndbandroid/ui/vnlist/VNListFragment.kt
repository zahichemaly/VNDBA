package com.booboot.vndbandroid.ui.vnlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.getThemeColor
import com.booboot.vndbandroid.extensions.hideOnBottom
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.postponeEnterTransitionIfExists
import com.booboot.vndbandroid.extensions.removeFocus
import com.booboot.vndbandroid.extensions.selectIf
import com.booboot.vndbandroid.extensions.setFocus
import com.booboot.vndbandroid.extensions.setStatusBarThemeForToolbar
import com.booboot.vndbandroid.extensions.setTextChangedListener
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.extensions.toggleBottomSheet
import com.booboot.vndbandroid.model.vndbandroid.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SORT_ID
import com.booboot.vndbandroid.model.vndbandroid.SORT_LENGTH
import com.booboot.vndbandroid.model.vndbandroid.SORT_POPULARITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_PRIORITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_RATING
import com.booboot.vndbandroid.model.vndbandroid.SORT_RELEASE_DATE
import com.booboot.vndbandroid.model.vndbandroid.SORT_STATUS
import com.booboot.vndbandroid.model.vndbandroid.SORT_VOTE
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.sort_bottom_sheet.*
import kotlinx.android.synthetic.main.vnlist_fragment.*

class VNListFragment : BaseFragment<VNListViewModel>(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    override val layout: Int = R.layout.vnlist_fragment
    lateinit var sortBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var sortBottomSheetButtons: List<View>
    private val adapter by lazy { VNAdapter(::onVnClicked, filteredVns = viewModel.filteredVns) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = home() ?: return

        savedInstanceState?.apply {
            setQuery(getString(SAVED_FILTER_STATE) ?: "", true)
        }

        setupStatusBar(true)
        setupToolbar()
        appBarLayout.setStatusBarThemeForToolbar(activity, toolbar)

        viewModel = ViewModelProviders.of(this).get(VNListViewModel::class.java)
        viewModel.restoreState(savedInstanceState)
        viewModel.vnlistData.observeNonNull(this, ::showVns)
        viewModel.sortData.observeOnce(this) {
            showSort()
            update()
        }
        viewModel.errorData.observeOnce(this, ::showError)
        home()?.viewModel?.accountData?.observeNonNull(this) { update() }
        home()?.viewModel?.filterData?.observeNonNull(this, ::filter)
        home()?.viewModel?.loadingData?.observeNonNull(this, ::showLoading)

        if (viewModel.vnlistData.value != null) {
            postponeEnterTransitionIfExists()
        }

        sortBottomSheetBehavior = BottomSheetBehavior.from(sortBottomSheet)
        sortBottomSheetBehavior.state = viewModel.sortBottomSheetState
        sortBottomSheetBehavior.onStateChanged(onStateChanged = { viewModel.sortBottomSheetState = it })

        filterBarLayout.isVisible = home()?.viewModel?.filterData?.value.isNullOrEmpty() == false
        filterBar.setTextChangedListener { setQuery(it) }
        filterBar.setText(home()?.viewModel?.filterData?.value)
        filterBarClear.setOnClickListener {
            if (filterBar.text.isNullOrEmpty()) {
                toggleFilterBar()
            } else {
                filterBar.text = null
            }
        }

        sortBottomSheetHeader.setOnClickListener(this)
        floatingSearchButton.setOnClickListener(this)

        sortBottomSheetButtons = listOf(buttonReverseSort, buttonSortID, buttonSortReleaseDate, buttonSortLength, buttonSortPopularity, buttonSortRating, buttonSortStatus, buttonSortVote, buttonSortPriority)
        sortBottomSheetButtons.forEach { it.setOnClickListener(this) }

        adapter.onUpdate = ::onAdapterUpdate
        vnList.setHasFixedSize(true)
        vnList.layoutManager = GridAutofitLayoutManager(activity, Pixels.px(300))
        vnList.adapter = adapter
        vnList.hideOnBottom(floatingSearchButton)

        backgroundInfo.setButtonOnClickListener { findNavController().navigate(R.id.searchFragment) }
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeColors(activity.getThemeColor(R.attr.colorAccent))

        showSort()
    }

    private fun toggleFilterBar() {
        filterBarLayout.toggle()
        if (filterBarLayout.isVisible) {
            filterBar?.setFocus()
        } else {
            filterBar?.text = null
            filterBar?.removeFocus()
        }
    }

    private fun update(force: Boolean = true) = viewModel.getVns(force)

    private fun showVns(accountItems: AccountItems) {
        adapter.filterString = home()?.viewModel?.filterData?.value ?: ""
        adapter.items = accountItems

        view?.post {
            startPostponedEnterTransition()
        }
    }

    override fun onAdapterUpdate(empty: Boolean) {
        super.onAdapterUpdate(empty)
        viewModel.filteredVns = adapter.filteredVns
    }

    private fun filter(search: CharSequence) {
        adapter.filter.filter(search)
    }

    override fun onRefresh() {
        home()?.startupSync()
    }

    override fun scrollToTop() {
        vnList.scrollToPosition(0)
    }

    private fun showSort() {
        buttonReverseSort?.selectIf(Preferences.reverseSort, R.color.textColorPrimaryBlack)
        buttonSortID?.selectIf(Preferences.sort == SORT_ID, R.color.textColorPrimaryReverse)
        buttonSortReleaseDate?.selectIf(Preferences.sort == SORT_RELEASE_DATE, R.color.textColorPrimaryReverse)
        buttonSortLength?.selectIf(Preferences.sort == SORT_LENGTH, R.color.textColorPrimaryReverse)
        buttonSortPopularity?.selectIf(Preferences.sort == SORT_POPULARITY, R.color.textColorPrimaryReverse)
        buttonSortRating?.selectIf(Preferences.sort == SORT_RATING, R.color.textColorPrimaryReverse)
        buttonSortStatus?.selectIf(Preferences.sort == SORT_STATUS, R.color.textColorPrimaryReverse)
        buttonSortVote?.selectIf(Preferences.sort == SORT_VOTE, R.color.textColorPrimaryReverse)
        buttonSortPriority?.selectIf(Preferences.sort == SORT_PRIORITY, R.color.textColorPrimaryReverse)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_tabs_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> sortBottomSheet.toggleBottomSheet()
            R.id.action_filter -> toggleFilterBar()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.sortBottomSheetHeader -> sortBottomSheet.toggleBottomSheet()
            R.id.buttonReverseSort -> viewModel.reverseSort()
            R.id.buttonSortID -> viewModel.setSort(SORT_ID)
            R.id.buttonSortReleaseDate -> viewModel.setSort(SORT_RELEASE_DATE)
            R.id.buttonSortLength -> viewModel.setSort(SORT_LENGTH)
            R.id.buttonSortPopularity -> viewModel.setSort(SORT_POPULARITY)
            R.id.buttonSortRating -> viewModel.setSort(SORT_RATING)
            R.id.buttonSortStatus -> viewModel.setSort(SORT_STATUS)
            R.id.buttonSortVote -> viewModel.setSort(SORT_VOTE)
            R.id.buttonSortPriority -> viewModel.setSort(SORT_PRIORITY)
            R.id.floatingSearchButton -> findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun query() = home()?.viewModel?.filterData?.value

    private fun setQuery(search: String, setOnlyIfNull: Boolean = false) {
        if (!setOnlyIfNull || query() == null) {
            home()?.viewModel?.filterData?.value = search
        }
    }

    companion object {
        const val SAVED_FILTER_STATE = "SAVED_FILTER_STATE"
    }
}