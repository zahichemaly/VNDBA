package com.booboot.vndbandroid.ui.hometabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.TAB_VALUE_ARG
import com.booboot.vndbandroid.ui.vnlist.VNListFragment

class HomeTabsAdapter(fm: FragmentManager?, private val numOfTabs: Int, private val type: Int) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        val tab = VNListFragment()

        when (position) {
            0 -> args.putInt(TAB_VALUE_ARG, getTabValue(Status.PLAYING, 10, Priority.HIGH))
            1 -> args.putInt(TAB_VALUE_ARG, getTabValue(Status.FINISHED, 8, Priority.MEDIUM))
            2 -> args.putInt(TAB_VALUE_ARG, getTabValue(Status.STALLED, 6, Priority.LOW))
            3 -> args.putInt(TAB_VALUE_ARG, getTabValue(Status.DROPPED, 4, Priority.BLACKLIST))
            4 -> args.putInt(TAB_VALUE_ARG, getTabValue(Status.UNKNOWN, 2, -1))
        }

        args.putInt(HomeTabsFragment.LIST_TYPE_ARG, type)
        tab.arguments = args
        return tab
    }

    private fun getTabValue(vnlistValue: Int, votelistValue: Int, wishlistValue: Int): Int = when (type) {
        HomeTabsFragment.VNLIST -> vnlistValue
        HomeTabsFragment.VOTELIST -> votelistValue
        HomeTabsFragment.WISHLIST -> wishlistValue
        else -> -1
    }

    override fun getCount(): Int = numOfTabs
}