package com.booboot.vndbandroid.ui.hometabs

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.ui.base.BaseFragmentStatePagerAdapter
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.TAB_VALUE_ARG
import com.booboot.vndbandroid.ui.vnlist.VNListFragment

class HomeTabsAdapter(fm: FragmentManager, private val type: Int) : BaseFragmentStatePagerAdapter(fm) {
    var titles = emptyList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int) = (fragments[position] ?: VNListFragment()).apply {
        arguments = Bundle().apply {
            putInt(TAB_VALUE_ARG, getTabValue(position))
            putInt(HomeTabsFragment.LIST_TYPE_ARG, type)
        }
    }

    private fun getTabValue(position: Int) = when (position) {
        0 -> getTabValue(Status.PLAYING, 10, Priority.HIGH)
        1 -> getTabValue(Status.FINISHED, 8, Priority.MEDIUM)
        2 -> getTabValue(Status.STALLED, 6, Priority.LOW)
        3 -> getTabValue(Status.DROPPED, 4, Priority.BLACKLIST)
        else -> getTabValue(Status.UNKNOWN, 2, -1)
    }

    private fun getTabValue(vnlistValue: Int, votelistValue: Int, wishlistValue: Int): Int = when (type) {
        HomeTabsFragment.VNLIST -> vnlistValue
        HomeTabsFragment.VOTELIST -> votelistValue
        HomeTabsFragment.WISHLIST -> wishlistValue
        else -> -1
    }

    override fun getCount(): Int = titles.size

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}