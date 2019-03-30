package com.booboot.vndbandroid.extensions

import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.booboot.vndbandroid.ui.home.HomeActivity
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.home_tabs_fragment.*

fun Fragment.home() = activity as? HomeActivity?

fun Fragment.setupToolbar() {
    home()?.setSupportActionBar(toolbar)
    home()?.setupActionBarWithNavController(findNavController(), home()?.drawer)
    toolbar.setNavigationOnClickListener { home()?.onSupportNavigateUp() }
}

fun Fragment.setupStatusBar(drawBehind: Boolean = false) = activity?.let { activity ->
    val toolbar = if (!drawBehind) {
        activity.window.statusBarColor = Color.TRANSPARENT
        view
    } else {
        toolbar
    }

    val rootParams = toolbar?.layoutParams as? ViewGroup.MarginLayoutParams
    rootParams?.topMargin = activity.statusBarHeight()
}

fun LifecycleOwner.actualOwner() = (this as? Fragment)?.viewLifecycleOwner ?: this

fun Fragment.startParentEnterTransition() = view?.doOnNextLayout {
    parentFragment?.startPostponedEnterTransition()
}

fun Fragment.postponeEnterTransitionIfExists(viewModel: BaseViewModel) {
    if (viewModel.hasPendingTransition) postponeEnterTransition()
    viewModel.hasPendingTransition = false
}