package com.booboot.vndbandroid.extensions

import com.google.android.material.tabs.TabLayout

fun TabLayout.updateTabs(titles: List<String>) {
    when {
        tabCount <= 0 -> // INIT
            titles.forEach { addTab(newTab().setText(it)) }
        tabCount == titles.size -> // UPDATE
            for (i in 0 until tabCount) getTabAt(i)?.text = titles[i]
        else -> {
            removeAllTabs()
            updateTabs(titles)
        }
    }
}