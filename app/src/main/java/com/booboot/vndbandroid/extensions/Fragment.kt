package com.booboot.vndbandroid.extensions

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.ui.home.HomeActivity
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.preferences_fragment.*

fun Fragment.home() = activity as? HomeActivity?

fun Fragment.setupToolbar() {
    home()?.setSupportActionBar(toolbar)
    ActionBarDrawerToggle(activity, home()?.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close).apply {
        home()?.drawer?.addDrawerListener(this)
        syncState()
    }
}