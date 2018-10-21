package com.booboot.vndbandroid.ui.hometabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.onStateChanged
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.extensions.updateTabs
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.home_tabs_fragment.*
import kotlinx.android.synthetic.main.vn_list_sort_bottom_sheet.*

class HomeTabsFragment : BaseFragment(), TabLayout.OnTabSelectedListener, View.OnClickListener {
    override val layout: Int = R.layout.home_tabs_fragment
    private lateinit var viewModel: HomeTabsViewModel
    private lateinit var sortBottomSheetBehavior: BottomSheetBehavior<View>

    private var type: Int = 0
    private var adapter: HomeTabsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        type = arguments?.getInt(LIST_TYPE_ARG) ?: VNLIST
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = activity ?: return
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(this)

        sortBottomSheetBehavior = BottomSheetBehavior.from(activity.sortBottomSheet)
        sortBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        sortBottomSheetBehavior.onStateChanged(
            onCollapsed = { sortBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN },
            onHidden = { activity.floatingSearchButton.show() },
            onExpanded = { activity.floatingSearchButton.hide() }
        )
        activity.sortBottomSheetHeader.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(HomeTabsViewModel::class.java)
        viewModel.titlesData.observe(this, Observer { showTitles(it) })
        viewModel.errorData.observe(this, Observer { showError(it) })
        viewModel.loadingData.observe(this, Observer { showLoading(it) })
        home()?.viewModel?.syncAccountData?.observe(this, Observer { update() })
        update(false)
    }

    private fun update(force: Boolean = true) = viewModel.getTabTitles(type, force)

    private fun showTitles(titles: List<String>?) {
        if (titles == null) return

        val tabLayoutEmpty = tabLayout.tabCount <= 0
        tabLayout.updateTabs(titles)

        if (tabLayoutEmpty) { // INIT
            adapter = HomeTabsAdapter(childFragmentManager, tabLayout.tabCount, type)
            viewPager.adapter = adapter
            if (currentPage >= 0) viewPager.currentItem = currentPage
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_tabs_fragment, menu)
        menu.findItem(R.id.action_filter)?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> sortBottomSheetBehavior.toggle()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.sortBottomSheetHeader -> sortBottomSheetBehavior.toggle()
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        viewPager.currentItem = tab.position
        currentPage = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {}

    override fun onTabReselected(tab: TabLayout.Tab) {}

    companion object {
        const val LIST_TYPE_ARG = "LIST_TYPE_ARG"
        const val TAB_VALUE_ARG = "TAB_VALUE_ARG"
        const val VNLIST = 1
        const val VOTELIST = 2
        const val WISHLIST = 3

        var currentPage = 0
    }
}