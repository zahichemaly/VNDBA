package com.booboot.vndbandroid.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.booboot.vndbandroid.ui.home.HomeActivity
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.home_tabs_fragment.*

fun Fragment.home() = activity as? HomeActivity?

fun Fragment.setupToolbar() {
    home()?.setSupportActionBar(toolbar)
    home()?.setupActionBarWithNavController(findNavController(), home()?.drawer)
    toolbar.setNavigationOnClickListener { home()?.onSupportNavigateUp() }
}