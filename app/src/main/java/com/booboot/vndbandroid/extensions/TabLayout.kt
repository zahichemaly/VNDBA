package com.booboot.vndbandroid.extensions

import com.google.android.material.tabs.TabLayout

fun TabLayout.replaceOnTabSelectedListener(listener: TabLayout.OnTabSelectedListener) {
    removeOnTabSelectedListener(listener)
    addOnTabSelectedListener(listener)
}