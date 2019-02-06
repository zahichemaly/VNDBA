package com.booboot.vndbandroid.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.extensions.Track
import com.booboot.vndbandroid.extensions.isTopLevel
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.setLightStatusAndNavigation
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.EVENT_VNLIST_CHANGED
import com.booboot.vndbandroid.model.vndbandroid.NOT_SET
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.service.EventReceiver
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment
import com.booboot.vndbandroid.ui.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity(), View.OnClickListener, SearchView.OnQueryTextListener {
    lateinit var viewModel: HomeViewModel

    private var searchView: SearchView? = null
    var savedFilter: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        setLightStatusAndNavigation()

        if (!Preferences.loggedIn || Preferences.gdprCrashlytics == NOT_SET) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            navigationView.setupWithNavController(findNavController(R.id.navHost))

            var shouldSync = true
            intent.extras?.apply {
                shouldSync = getBoolean(SHOULD_SYNC, true)
                remove(SHOULD_SYNC)
            }
            savedInstanceState?.apply {
                savedFilter = getString(SAVED_FILTER_STATE) ?: ""
            }

            viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
            viewModel.loadingData.observe(this, Observer { showLoading(it) })
            viewModel.accountData.observe(this, Observer { showAccount(it) })
            viewModel.syncAccountData.observeOnce(this, ::showAccount)
            viewModel.errorData.observeOnce(this, ::showError)
            viewModel.getVns()

            if (supportFragmentManager.primaryNavigationFragment == null) {
                if (shouldSync) viewModel.startupSync()
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

    private fun showAccount(accountItems: AccountItems?) {
        accountItems ?: return

        Track.tag(accountItems)
        setMenuCounter(R.id.vnlistFragment, accountItems.vnlist.size)
        setMenuCounter(R.id.votelistFragment, accountItems.wishlist.size)
        setMenuCounter(R.id.wishlistFragment, accountItems.votelist.size)
    }

    // TODO make this work with setupWithNavController without opening a Fragment
    private fun logout(): Boolean {
        // TODO clear all
        VNDBServer.closeAll().subscribe()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_activity, menu)

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

    override fun onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return
        }

        val fragment = supportFragmentManager.primaryNavigationFragment
        if (fragment is HomeTabsFragment && fragment.sortBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            fragment.sortBottomSheetBehavior.toggle()
            return
        }

        if (searchView?.isIconified == false) {
            searchView?.isIconified = true
            return
        }

        val topLevelDestinations = AppBarConfiguration.Builder(findNavController(R.id.navHost).graph).setDrawerLayout(drawer).build().topLevelDestinations
        if (drawer != null && findNavController(R.id.navHost).currentDestination?.isTopLevel(topLevelDestinations) == true) {
            /* Going back to home screen (don't use super.onBackPressed() because it would kill the app) */
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        } else super.onBackPressed()
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(findNavController(R.id.navHost), drawer)

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SAVED_FILTER_STATE, searchView?.query?.toString() ?: "")
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val SAVED_FILTER_STATE = "SAVED_FILTER_STATE"
        const val SHOULD_SYNC = "SHOULD_SYNC"
    }
}