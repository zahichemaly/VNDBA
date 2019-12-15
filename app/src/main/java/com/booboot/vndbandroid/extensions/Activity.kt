package com.booboot.vndbandroid.extensions

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.booboot.vndbandroid.R

/**
 * Sets the light status and navigation bar when in Day mode.
 * Resets the status and navigation to default (dark) when in Night mode.
 * Cannot use conditional styles for this as there is an Android bug: when switching from Day to Night mode,
 * Android isn't able to reset these flags. Hence the programmatical reset.
 */
fun Activity.setLightStatusAndNavigation() {
    val flags = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_UNDEFINED, Configuration.UI_MODE_NIGHT_NO -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else -> 0
    } or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    window.decorView.systemUiVisibility = flags
}

fun AppCompatActivity.currentFragment() = supportFragmentManager.findFragmentById(R.id.navHost)?.childFragmentManager?.primaryNavigationFragment

fun Activity.isTopLevel(): Boolean {
    val topLevelDestinations = AppBarConfiguration.Builder(findNavController(R.id.navHost).graph).build().topLevelDestinations
    return findNavController(R.id.navHost).currentDestination?.isTopLevel(topLevelDestinations) == true
}

fun Activity.removeFocus() = currentFocus?.removeFocus()