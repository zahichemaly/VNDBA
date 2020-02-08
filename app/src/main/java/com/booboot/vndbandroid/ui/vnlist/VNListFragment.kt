package com.booboot.vndbandroid.ui.vnlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.getThemeColor
import com.booboot.vndbandroid.extensions.home
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.postponeEnterTransitionIfExists
import com.booboot.vndbandroid.extensions.removeFocus
import com.booboot.vndbandroid.extensions.selectIf
import com.booboot.vndbandroid.extensions.setupStatusBar
import com.booboot.vndbandroid.extensions.setupToolbar
import com.booboot.vndbandroid.extensions.toggleBottomSheet
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
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.util.GridAutofitLayoutManager
import com.booboot.vndbandroid.util.Pixels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.filter_bottom_sheet.*
import kotlinx.android.synthetic.main.floating_search_toolbar.*
import kotlinx.android.synthetic.main.floating_search_toolbar.view.*
import kotlinx.android.synthetic.main.vnlist_fragment.*
import me.zhanghai.android.fastscroll.FastScrollerBuilder

class VNListFragment : BaseFragment<VNListViewModel>(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    override val layout: Int = R.layout.vnlist_fragment
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val adapter by lazy { VNAdapter(::onVnClicked) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = home() ?: return

        setupStatusBar(true, searchBarCardView)
        setupToolbar()
        floatingSearchBar.setupWithContainer(constraintLayout, vnList)

        viewModel = ViewModelProvider(this).get(VNListViewModel::class.java)
        viewModel.restoreState(savedInstanceState)
        viewModel.vnlistData.observeNonNull(this, ::showVns)
        viewModel.scrollToTopData.observeOnce(this) { if (it) scrollToTop() }
        viewModel.errorData.observeOnce(this, ::showError)
        home()?.viewModel?.accountData?.observeNonNull(this) { update() }
        home()?.viewModel?.loadingData?.observeNonNull(this, ::showLoading)

        if (viewModel.vnlistData.value != null) {
            postponeEnterTransitionIfExists()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.onStateChanged(
            onCollapsed = {
                removeFocus()
                iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp)
            },
            onExpanding = { iconArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp) }
        )

        floatingSearchBar.setTextChangedListener { viewModel.getVns(_filter = it, scrollToTop = floatingSearchBar.searchBar.hasFocus()) }
        bottomSheetHeader.setOnClickListener(this)

        listOf<View>(buttonReverseSort, buttonSortID, buttonSortTitle, buttonSortReleaseDate, buttonSortLength, buttonSortPopularity, buttonSortRating, buttonSortStatus, buttonSortVote, buttonSortPriority).forEach {
            it.setOnClickListener(this)
        }

        adapter.onUpdate = ::onAdapterUpdate
        vnList.setHasFixedSize(true)
        vnList.layoutManager = GridAutofitLayoutManager(activity, Pixels.px(300))
        vnList.adapter = adapter
        FastScrollerBuilder(vnList).useMd2Style().build()

        backgroundInfo.setButtonOnClickListener { findNavController().navigate(R.id.searchFragment) }
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeColors(activity.getThemeColor(R.attr.colorAccent))
    }

    private fun update() = viewModel.getVns(scrollToTop = false)

    private fun showVns(vnlistData: VnlistData) {
        adapter.sort = Preferences.sort
        adapter.data = vnlistData

        buttonReverseSort.isChecked = Preferences.reverseSort
        buttonSortID?.selectIf(Preferences.sort == SORT_ID, R.color.textColorPrimaryReverse)
        buttonSortTitle?.selectIf(Preferences.sort == SORT_TITLE, R.color.textColorPrimaryReverse)
        buttonSortReleaseDate?.selectIf(Preferences.sort == SORT_RELEASE_DATE, R.color.textColorPrimaryReverse)
        buttonSortLength?.selectIf(Preferences.sort == SORT_LENGTH, R.color.textColorPrimaryReverse)
        buttonSortPopularity?.selectIf(Preferences.sort == SORT_POPULARITY, R.color.textColorPrimaryReverse)
        buttonSortRating?.selectIf(Preferences.sort == SORT_RATING, R.color.textColorPrimaryReverse)
        buttonSortStatus?.selectIf(Preferences.sort == SORT_STATUS, R.color.textColorPrimaryReverse)
        buttonSortVote?.selectIf(Preferences.sort == SORT_VOTE, R.color.textColorPrimaryReverse)
        buttonSortPriority?.selectIf(Preferences.sort == SORT_PRIORITY, R.color.textColorPrimaryReverse)

        view?.post {
            startPostponedEnterTransition()
        }
    }

    override fun onRefresh() {
        home()?.startupSync()
    }

    override fun scrollToTop() {
        vnList.scrollToPosition(0)
        floatingSearchBar.setExpanded(true, true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.vnlist_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> findNavController().navigate(R.id.searchFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bottomSheetHeader -> bottomSheet.toggleBottomSheet()
            R.id.buttonReverseSort -> viewModel.getVns(_reverseSort = !Preferences.reverseSort)
            R.id.buttonSortID -> viewModel.getVns(_sort = SORT_ID)
            R.id.buttonSortTitle -> viewModel.getVns(_sort = SORT_TITLE)
            R.id.buttonSortReleaseDate -> viewModel.getVns(_sort = SORT_RELEASE_DATE)
            R.id.buttonSortLength -> viewModel.getVns(_sort = SORT_LENGTH)
            R.id.buttonSortPopularity -> viewModel.getVns(_sort = SORT_POPULARITY)
            R.id.buttonSortRating -> viewModel.getVns(_sort = SORT_RATING)
            R.id.buttonSortStatus -> viewModel.getVns(_sort = SORT_STATUS)
            R.id.buttonSortVote -> viewModel.getVns(_sort = SORT_VOTE)
            R.id.buttonSortPriority -> viewModel.getVns(_sort = SORT_PRIORITY)
        }
    }
}