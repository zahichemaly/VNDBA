package com.booboot.vndbandroid.ui.base

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

abstract class BaseFragmentStatePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    protected val fragments = mutableMapOf<Int, BaseFragment<*>>()

    override fun instantiateItem(container: ViewGroup, position: Int) = super.instantiateItem(container, position).apply {
        fragments[position] = this as BaseFragment<*>
    }

    fun getFragment(position: Int) = fragments[position]
}