package com.booboot.vndbandroid.ui.hometabs

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.ui.base.BaseFragment
import kotlinx.android.synthetic.main.home_tabs_fragment.*

/**
 * Created by od on 13/03/2016.
 */
class HomeTabsFragment : BaseFragment(), TabLayout.OnTabSelectedListener {
    override val layout: Int = R.layout.home_tabs_fragment
    private lateinit var homeTabsViewModel: HomeTabsViewModel

    private var type: Int = 0
    private lateinit var adapter: HomeTabsAdapter

    val registeredFragments: Map<Int, Fragment>
        get() = adapter.registeredFragments

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        type = arguments?.getInt(LIST_TYPE_ARG) ?: VNLIST

        homeTabsViewModel = ViewModelProviders.of(this).get(HomeTabsViewModel::class.java)
        homeTabsViewModel.titlesData.observe(this, Observer { showTitles(it) })
        homeTabsViewModel.errorData.observe(this, Observer { showError(it) })
        homeTabsViewModel.getTabTitles(type)

        return rootView
    }

    private fun showTitles(titles: List<String>?) {
        if (titles == null) return
        if (tabLayout.tabCount <= 0) { // INIT
            titles.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }

            adapter = HomeTabsAdapter(childFragmentManager, tabLayout.tabCount, type)
            viewPager.adapter = adapter
            viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.addOnTabSelectedListener(this)

            if (currentPage >= 0)
                viewPager.currentItem = currentPage
            else
                currentPage = 0
        } else { // UPDATE
            for (i in 0 until tabLayout.tabCount) tabLayout.getTabAt(i)?.text = titles[i]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.vn_list_fragment, menu)
        menu?.findItem(R.id.action_filter)?.isVisible = true
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

        var currentPage = -1
    }
}