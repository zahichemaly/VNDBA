package com.booboot.vndbandroid.extensions

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
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

fun Fragment.setTransparentStatusBar() = activity?.let { activity ->
    activity.window.statusBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
    val rootParams = view?.layoutParams as? ViewGroup.MarginLayoutParams
    rootParams?.topMargin = activity.statusBarHeight()
}

fun LifecycleOwner.actualOwner() = (this as? Fragment)?.viewLifecycleOwner ?: this