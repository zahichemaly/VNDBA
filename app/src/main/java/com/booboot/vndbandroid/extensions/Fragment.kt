package com.booboot.vndbandroid.extensions

import android.graphics.Color
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.booboot.vndbandroid.ui.base.BaseAdapter
import com.booboot.vndbandroid.ui.base.BaseViewModel
import com.booboot.vndbandroid.ui.base.HasTabs
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

fun Fragment.startParentEnterTransition(adapter: BaseAdapter<*>? = null) {
    val finalParentFragment = parentFragment ?: return
    if (finalParentFragment is HasTabs && finalParentFragment.currentFragmentClass() != javaClass) return

    if (adapter != null) {
        adapter.onFinishDrawing = {
            parentFragment?.startPostponedEnterTransition()
        }
    } else view?.post {
        parentFragment?.startPostponedEnterTransition()
    }
}

fun Fragment.postponeEnterTransitionIfExists(viewModel: BaseViewModel) {
    if (viewModel.hasPendingTransition) postponeEnterTransition()
    viewModel.hasPendingTransition = false
}