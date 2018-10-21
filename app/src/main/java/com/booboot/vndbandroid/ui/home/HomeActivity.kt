package com.booboot.vndbandroid.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.extensions.Track
import com.booboot.vndbandroid.extensions.reset
import com.booboot.vndbandroid.extensions.setLightStatusAndNavigation
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.EVENT_VNLIST_CHANGED
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.service.EventReceiver
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment
import com.booboot.vndbandroid.ui.login.LoginActivity
import com.booboot.vndbandroid.ui.preferences.PreferencesFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.vn_list_sort_bottom_sheet.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SearchView.OnQueryTextListener {
    lateinit var viewModel: HomeViewModel
    @Inject lateinit var accountRepository: AccountRepository

    private var searchView: SearchView? = null
    var savedFilter: String = ""
    private var selectedItem: Int = 0
    private lateinit var sortBottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        (application as App).appComponent.inject(this)
        setLightStatusAndNavigation()

        setSupportActionBar(toolbar)

        if (!Preferences.loggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            if (drawer != null) {
                drawer.addDrawerListener(toggle)
            }
            toggle.syncState()

            navigationView.setNavigationItemSelectedListener(this)
            sortBottomSheetBehavior = BottomSheetBehavior.from(sortBottomSheet)

            var shouldSync = true
            intent.extras?.apply {
                shouldSync = getBoolean(SHOULD_SYNC, true)
                remove(SHOULD_SYNC)
            }
            savedInstanceState?.apply {
                selectedItem = getInt(SELECTED_ITEM)
                savedFilter = getString(SAVED_FILTER_STATE) ?: ""
            }

            viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
            viewModel.loadingData.observe(this, Observer { showLoading(it) })
            viewModel.accountData.observe(this, Observer { updateMenuCounters(it) })
            viewModel.syncAccountData.observe(this, Observer { updateMenuCounters(it) })
            viewModel.errorData.observe(this, Observer { showError(it, viewModel.errorData) })

            floatingSearchButton.setOnClickListener(this)
            viewModel.getVns()

            val oldFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT)

            if (oldFragment == null) {
                navigationView.menu.getItem(0).isChecked = true
                onNavigationItemSelected(navigationView.menu.getItem(0))
                if (shouldSync) viewModel.startupSync()
            } else {
                enableToolbarScroll(oldFragment is HomeTabsFragment)
            }

            EventReceiver(this).observe(mapOf(
                EVENT_VNLIST_CHANGED to { viewModel.getVns(toSyncData = true) }
            ))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun startupSync() = viewModel.startupSync()

    fun updateSyncAccountData() {
        viewModel.syncAccountData.value = viewModel.accountData.value
    }

    private fun updateMenuCounters(accountItems: AccountItems?) {
        accountItems ?: return
        Track.tag(accountItems)
        setMenuCounter(R.id.nav_vnlist, accountItems.vnlist.size)
        setMenuCounter(R.id.nav_wishlist, accountItems.wishlist.size)
        setMenuCounter(R.id.nav_votelist, accountItems.votelist.size)
        viewModel.syncAccountData.reset()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = goToFragment(item.itemId)

    fun goToFragment(id: Int): Boolean {
        val args = Bundle()
        selectedItem = id

        val fragment: Fragment = when (id) {
            R.id.nav_vnlist -> {
                args.putInt(HomeTabsFragment.LIST_TYPE_ARG, HomeTabsFragment.VNLIST)
                HomeTabsFragment()
            }
            R.id.nav_votelist -> {
                args.putInt(HomeTabsFragment.LIST_TYPE_ARG, HomeTabsFragment.VOTELIST)
                HomeTabsFragment()
            }
            R.id.nav_wishlist -> {
                args.putInt(HomeTabsFragment.LIST_TYPE_ARG, HomeTabsFragment.WISHLIST)
                HomeTabsFragment()
            }
            R.id.nav_preferences -> PreferencesFragment()
            //            R.id.nav_stats -> DatabaseStatisticsFragment()
            //            R.id.nav_top -> RankingTopFragment()
            //            R.id.nav_popular -> RankingPopularFragment()
            //            R.id.nav_most_voted -> RankingMostVotedFragment()
            //            R.id.nav_newly_released -> RankingNewlyReleasedFragment()
            //            R.id.nav_newly_added -> RankingNewlyAddedFragment()
            //            R.id.nav_recommendations -> RecommendationsFragment()
            //            R.id.nav_about -> AboutFragment()
            R.id.nav_logout -> return logout()
            else -> null
        } ?: return false

        fragment.arguments = args
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, TAG_FRAGMENT).addToBackStack(null).commit()
        drawer?.closeDrawer(GravityCompat.START)
        floatingSearchButton.toggle(id != R.id.nav_preferences)
        enableToolbarScroll(id in listOf(R.id.nav_vnlist, R.id.nav_votelist, R.id.nav_wishlist))

        return true
    }

    private fun logout(): Boolean {
        // TODO clear all
        VNDBServer.closeAll().subscribe()

        startActivity(Intent(this, LoginActivity::class.java))
        selectedItem = 0
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)

        /* Filter */
        searchView = menu.findItem(R.id.action_filter).actionView as SearchView

        if (savedFilter.isNotEmpty()) {
            searchView?.isIconified = false
            searchView?.setQuery(savedFilter, true)
        }

        searchView?.setOnQueryTextListener(this)
        searchView?.clearFocus()

        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(search: String): Boolean {
        savedFilter = search
        viewModel.filterData.value = search
        return true
    }

    override fun onClick(v: View?) = when (v?.id) {
        R.id.floatingSearchButton -> {
//            startActivity(Intent(this, VNSearchActivity::class.java))
        }
        else -> {
        }
    }

    private fun setMenuCounter(itemId: Int, count: Int) {
        navigationView?.let {
            val view = it.menu.findItem(itemId).actionView as TextView
            view.text = if (count > 0) count.toString() else null
        }
    }

    fun enableToolbarScroll(enabled: Boolean) {
        val params = toolbar?.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = if (enabled) AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS else 0
    }

    override fun onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (sortBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sortBottomSheetBehavior.toggle()
        } else if (searchView?.isIconified == false) {
            searchView?.isIconified = true
        } else {
            /* Going back to home screen (don't use super.onBackPressed() because it would redirect to the LoginActivity underneath) */
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM, selectedItem)
        outState.putString(SAVED_FILTER_STATE, searchView?.query.toString())
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val TAG_FRAGMENT = "TAG_FRAGMENT"
        const val SELECTED_ITEM = "SELECTED_ITEM"
        const val SAVED_FILTER_STATE = "SAVED_FILTER_STATE"
        const val SHOULD_SYNC = "SHOULD_SYNC"
    }
}