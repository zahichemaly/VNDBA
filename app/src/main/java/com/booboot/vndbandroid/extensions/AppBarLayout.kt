package com.booboot.vndbandroid.extensions

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.booboot.vndbandroid.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

fun AppBarLayout.setStatusBarThemeForCollapsingToolbar(activity: AppCompatActivity, collapsingToolbar: CollapsingToolbarLayout, toolbar: Toolbar, contentView: View) =
    addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
        if (collapsingToolbar.height + offset < collapsingToolbar.scrimVisibleHeightTrigger) {
            /* Toolbar is collapsing: removing the status bar's translucence and adding the light flag */
            activity.window.statusBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
            toolbar.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent))
        } else {
            /* Toolbar is expanding: adding the status bar's translucence and removing the light flag */
            activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.tabBackgroundColor)
            toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.tabBackgroundColor))
        }

        if (activity.dayNightTheme() == "light") {
            if (activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR == 0) {
                activity.window.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                /* Scroll is broken if we don't refresh layout on the sibling view for some reason */
                contentView.requestLayout()
            }
        }
    })

fun AppBarLayout.setStatusBarThemeForToolbar(activity: AppCompatActivity, toolbar: Toolbar) =
    addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
        if (activity.statusBarHeight() + toolbar.height + offset > 0) {
            /* Toolbar is collapsing: removing the status bar's translucence and adding the light flag */
            activity.window.statusBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
            toolbar.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent))
        } else {
            /* Toolbar is expanding: adding the status bar's translucence and removing the light flag */
            activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.tabBackgroundColor)
            toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.tabBackgroundColor))
        }
    })

fun AppBarLayout.fixForFastScroll(container: ViewGroup, scrollingChild: View? = null) {
    val initialPaddingBottom = container.paddingBottom
    val initialPaddingTop = scrollingChild?.paddingTop ?: 0
    addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
        container.setPaddingBottom(initialPaddingBottom + totalScrollRange + offset)
        scrollingChild?.setPaddingTop(initialPaddingTop - offset)
    })
}