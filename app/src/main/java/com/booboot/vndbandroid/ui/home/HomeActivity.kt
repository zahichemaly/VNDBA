package com.booboot.vndbandroid.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.extensions.Track
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.base.BaseFragment
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment
import com.booboot.vndbandroid.ui.login.LoginActivity
import com.booboot.vndbandroid.ui.preferences.PreferencesFragment
import com.booboot.vndbandroid.ui.vnlist.VNListFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private lateinit var viewModel: HomeViewModel
    @Inject lateinit var accountRepository: AccountRepository

    var searchView: SearchView? = null
    private var savedFilter: String? = null
    var selectedItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        (application as App).appComponent.inject(this)

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

            //            navigationView.menu.findItem(R.id.accountTitle).setTitle(SettingsManager.getUsername(this))

//            val header = navigationView.getHeaderView(0)
//            val headerBackground = header.findViewById<ImageView>(R.id.headerBackground)
            //            Picasso.get().load(Theme.THEMES.get(SettingsManager.getTheme(this)).getWallpaper()).transform(BlurIfDemoTransform(this)).into(headerBackground)

            if (savedInstanceState != null) {
                selectedItem = savedInstanceState.getInt(SELECTED_ITEM)
                savedFilter = savedInstanceState.getString(SAVED_FILTER_STATE)
            }

            viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
            viewModel.loadingData.observe(this, Observer { showLoading(it) })
            viewModel.syncAccountData.observe(this, Observer { showResult(it) })
            viewModel.accountData.observe(this, Observer { updateMenuCounters(it) })
            viewModel.errorData.observe(this, Observer { showError(it) })

            floatingSearchButton.setOnClickListener(this)
            viewModel.getVns(false)

            val oldFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT)

            if (oldFragment == null) {
                navigationView.menu.getItem(0).isChecked = true
                onNavigationItemSelected(navigationView.menu.getItem(0))
                viewModel.startupSync()
            } else {
                enableToolbarScroll(oldFragment is HomeTabsFragment)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading(show: Boolean?) {
        if (show == null) return

        val currentFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT)
        when (currentFragment) {
            is HomeTabsFragment -> currentFragment.registeredFragments.values.forEach { (it as? VNListFragment)?.showLoading(show) }
            is BaseFragment -> currentFragment.showLoading(show)
            else -> super.showLoading(show)
        }
    }

    fun isLoading() = viewModel.loadingData.value == true

    fun startupSync() = viewModel.startupSync()

    private fun showResult(result: AccountItems?) {
        if (result == null) return
//        Logger.log(result.toString())
        val currentFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT)
        when (currentFragment) {
            is HomeTabsFragment -> {
                currentFragment.update()
                currentFragment.registeredFragments.values.forEach { (it as? VNListFragment)?.update() }
            }
        }
    }

    private fun updateMenuCounters(accountItems: AccountItems?) {
        if (accountItems == null) return
        Track.tag(accountItems)
        setMenuCounter(R.id.nav_vnlist, accountItems.vnlist.size)
        setMenuCounter(R.id.nav_wishlist, accountItems.wishlist.size)
        setMenuCounter(R.id.nav_votelist, accountItems.votelist.size)
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
        toggleFloatingSearchButton(id != R.id.nav_preferences)
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
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_filter).actionView as SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.queryHint = getString(R.string.action_filter_hint)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(search: String): Boolean {
                savedFilter = search
                val currentFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT) as? HomeTabsFragment
                    ?: return true

                currentFragment.registeredFragments.values.forEach { (it as? VNListFragment)?.filter(search) }
                return true
            }
        })

        if (savedFilter?.isNotEmpty() == true) {
            searchView?.isIconified = false
            searchView?.setQuery(savedFilter, true)
        }
        searchView?.clearFocus()

        val searchIconId = searchView?.context?.resources?.getIdentifier("android:id/search_button", null, null)
        val searchIcon = searchView?.findViewById<ImageView>(searchIconId ?: 0)
        searchIcon?.setImageResource(R.drawable.ic_filter_list_24dp)

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
        if (navigationView != null) {
            val view = navigationView.menu.findItem(itemId).actionView as TextView
            view.text = if (count > 0) count.toString() else null
        }
    }

    fun enableToolbarScroll(enabled: Boolean) {
        val params = toolbar?.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = if (enabled) AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS else 0
    }

    fun toggleFloatingSearchButton(show: Boolean) = if (show) floatingSearchButton.show() else floatingSearchButton.hide()

    override fun onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
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
    }
}