package com.booboot.vndbandroid.ui.base

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

abstract class BaseFragmentStatePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    protected val fragments = mutableMapOf<Int, Fragment>()

    override fun instantiateItem(container: ViewGroup, position: Int) = super.instantiateItem(container, position).apply {
        fragments[position] = this as Fragment
    }
}