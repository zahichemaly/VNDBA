package com.booboot.vndbandroid.ui.hometabs

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.booboot.vndbandroid.model.vndbandroid.HomeTab
import com.booboot.vndbandroid.ui.base.BaseFragmentStatePagerAdapter
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment.Companion.TAB_VALUE_ARG
import com.booboot.vndbandroid.ui.vnlist.VNListFragment

class HomeTabsAdapter(fm: FragmentManager, private val type: Int) : BaseFragmentStatePagerAdapter(fm) {
    var tabs = emptyList<HomeTab>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int) = (fragments[position] ?: VNListFragment()).apply {
        arguments = Bundle().apply {
            putLong(TAB_VALUE_ARG, tabs[position].value)
            putInt(HomeTabsFragment.LIST_TYPE_ARG, type)
        }
    }

    override fun getCount(): Int = tabs.size

    override fun getPageTitle(position: Int): CharSequence? = tabs[position].title
}