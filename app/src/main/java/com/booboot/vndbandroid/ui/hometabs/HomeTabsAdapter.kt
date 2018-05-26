package com.booboot.vndbandroid.ui.hometabs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.Status
import com.booboot.vndbandroid.ui.vnlist.VNListFragment

/**
 * Created by od on 13/03/2016.
 */
class HomeTabsAdapter(fm: FragmentManager?, private val numOfTabs: Int, private val type: Int) : FragmentStatePagerAdapter(fm) {
    val registeredFragments = mutableMapOf<Int, Fragment>()

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        val tab = VNListFragment()

        when (position) {
            0 -> args.putInt(VNListFragment.TAB_VALUE_ARG, getTabValue(Status.PLAYING, 10, Priority.HIGH))
            1 -> args.putInt(VNListFragment.TAB_VALUE_ARG, getTabValue(Status.FINISHED, 8, Priority.MEDIUM))
            2 -> args.putInt(VNListFragment.TAB_VALUE_ARG, getTabValue(Status.STALLED, 6, Priority.LOW))
            3 -> args.putInt(VNListFragment.TAB_VALUE_ARG, getTabValue(Status.DROPPED, 4, Priority.BLACKLIST))
            4 -> args.putInt(VNListFragment.TAB_VALUE_ARG, getTabValue(Status.UNKNOWN, 2, -1))
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

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments[position] = fragment
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, any)
    }
}