package com.booboot.vndbandroid.ui.base

import androidx.fragment.app.Fragment

interface HasTabs {
    fun currentFragmentClass(): Class<out Fragment>?
}