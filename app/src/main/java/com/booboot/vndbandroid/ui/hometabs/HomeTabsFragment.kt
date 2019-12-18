package com.booboot.vndbandroid.ui.hometabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.postponeEnterTransitionIfExists
import com.booboot.vndbandroid.extensions.removeFocus
import com.booboot.vndbandroid.extensions.replaceOnTabSelectedListener
import com.booboot.vndbandroid.extensions.selectIf
import com.booboot.vndbandroid.extensions.setFocus
import com.booboot.vndbandroid.extensions.setTextChangedListener
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.extensions.toggleBottomSheet
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.model.vndbandroid.SORT_ID
import com.booboot.vndbandroid.model.vndbandroid.SORT_LENGTH
import com.booboot.vndbandroid.model.vndbandroid.SORT_POPULARITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_PRIORITY
import com.booboot.vndbandroid.model.vndbandroid.SORT_RATING
import com.booboot.vndbandroid.model.vndbandroid.SORT_RELEASE_DATE
import com.booboot.vndbandroid.model.vndbandroid.SORT_STATUS
import com.booboot.vndbandroid.model.vndbandroid.SORT_VOTE
import com.booboot.vndbandroid.model.vndbandroid.VnlistData
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_tabs_fragment.*
import kotlinx.android.synthetic.main.sort_bottom_sheet.*

class HomeTabsFragment : BaseFragment<HomeTabsViewModel>(), TabLayout.OnTabSelectedListener, View.OnClickListener {
    override val layout: Int = R.layout.home_tabs_fragment
    lateinit var sortBottomSheetBehavior: BottomSheetBehavior<View>

    private var type: Int = 0
    private val tabsAdapter: HomeTabsAdapter by lazy { HomeTabsAdapter(childFragmentManager, type) }
    private lateinit var sortBottomSheetButtons: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        arguments?.let { arguments ->
            type = HomeTabsFragmentArgs.fromBundle(arguments).listType
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity ?: return

        savedInstanceState?.apply {
            setQuery(getString(SAVED_FILTER_STATE) ?: "", true)
        }

        setupStatusBar()
        setupToolbar()

        viewModel = ViewModelProviders.of(this).get(HomeTabsViewModel::class.java)
        viewModel.restoreState(savedInstanceState)
        viewModel.vnlistData.observeNonNull(this, ::showTabs)
        viewModel.sortData.observeOnce(this) {
            showSort()
            update()
        }
        viewModel.errorData.observeOnce(this, ::showError)
        home()?.viewModel?.accountData?.observeNonNull(this) { update() }

        if (viewModel.vnlistData.value?.tabs?.isNotEmpty() == true) {
            postponeEnterTransitionIfExists()
        }

        viewPager.adapter = tabsAdapter
        tabLayout.setupWithViewPager(viewPager)

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

    private fun update(force: Boolean = true) = viewModel.getTabTitles(type, force)

    private fun showTabs(vnlistData: VnlistData) {
        if (viewModel.currentPage >= 0) viewPager.currentItem = viewModel.currentPage
        tabsAdapter.tabs = vnlistData.tabs
        tabLayout.replaceOnTabSelectedListener(this)
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

    override fun onTabSelected(tab: TabLayout.Tab) {
        viewModel.currentPage = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {}

    override fun onTabReselected(tab: TabLayout.Tab) {
        tabsAdapter.getFragment(tab.position)?.scrollToTop()
    }

    override fun onDestroyView() {
        tabLayout?.removeOnTabSelectedListener(this)
        super.onDestroyView()
    }

    companion object {
        const val LIST_TYPE_ARG = "LIST_TYPE_ARG"
        const val TAB_VALUE_ARG = "TAB_VALUE_ARG"
        const val SAVED_FILTER_STATE = "SAVED_FILTER_STATE"
        const val VNLIST = 1
        const val VOTELIST = 2
        const val WISHLIST = 3
    }
}