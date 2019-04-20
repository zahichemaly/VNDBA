package com.booboot.vndbandroid.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.Track
import com.booboot.vndbandroid.extensions.isTopLevel
import com.booboot.vndbandroid.extensions.observe
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.setLightStatusAndNavigation
import com.booboot.vndbandroid.extensions.toggle
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.NOT_SET
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment
import com.booboot.vndbandroid.ui.login.LoginActivity
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity(), View.OnClickListener {
    lateinit var viewModel: HomeViewModel

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

            viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
            viewModel.loadingData.observe(this, ::showLoading)
            viewModel.accountData.observe(this, ::showAccount)
            viewModel.errorData.observeOnce(this, ::showError)
            viewModel.getVns()

            if (supportFragmentManager.primaryNavigationFragment == null) {
                if (shouldSync) viewModel.startupSync()
            }
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
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu) = true

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

        val fragment = supportFragmentManager.findFragmentById(R.id.navHost)?.childFragmentManager?.primaryNavigationFragment
        when (fragment) {
            is HomeTabsFragment -> if (fragment.sortBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                return fragment.sortBottomSheetBehavior.toggle()
            } else if (fragment.searchView?.isIconified == false) {
                fragment.searchView?.isIconified = true
                return
            }
            is VNDetailsFragment -> if (fragment.bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                return fragment.bottomSheetBehavior.toggle()
            }
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

    companion object {
        const val SHOULD_SYNC = "SHOULD_SYNC"
    }
}