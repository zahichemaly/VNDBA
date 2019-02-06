package com.booboot.vndbandroid.extensions

import androidx.navigation.NavDestination

fun NavDestination.isTopLevel(destinationIds: Set<Int>): Boolean {
    var currentDestination = this
    while (true) {
        if (currentDestination.id in destinationIds) return true
        currentDestination = currentDestination.parent ?: return false
    }
}