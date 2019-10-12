package com.booboot.vndbandroid.extensions

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.ui.home.HomeActivity
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.home_tabs_fragment.*

fun Fragment.home() = activity as? HomeActivity?

fun Fragment.setupToolbar() {
    home()?.setSupportActionBar(toolbar)
    home()?.setupActionBarWithNavController(findNavController(), home()?.drawer)
}

fun Fragment.setupStatusBar(drawBehind: Boolean = false) = view?.post {
    val activity = activity ?: return@post
    val toolbar = if (!drawBehind) {
        activity.window.statusBarColor = Color.TRANSPARENT
        view
    } else {
        activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.tabBackgroundColor)
        toolbar
    }

    toolbar?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        val appBarLayoutParams = (toolbar.parent as? View)?.layoutParams
        if (appBarLayoutParams is FrameLayout.LayoutParams && appBarLayoutParams.gravity == Gravity.BOTTOM) return@updateLayoutParams
        topMargin = activity.statusBarHeight()
    }
    toolbar?.isVisible = true
}

fun LifecycleOwner.actualOwner() = (this as? Fragment)?.viewLifecycleOwner ?: this

fun Fragment.startParentEnterTransition() = view?.post {
    parentFragment?.startPostponedEnterTransition()
}

fun Fragment.postponeEnterTransitionIfExists() {
    if (Preferences.useSharedTransitions) postponeEnterTransition()
}