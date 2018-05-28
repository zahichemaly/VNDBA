package com.booboot.vndbandroid.ui.vndetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.booboot.vndbandroid.model.vndb.VN

/**
 * Created by od on 13/03/2016.
 */
class VNDetailsTabsAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private var numOfTabs = 0
    var vn: VN? = null
        set(value) {
            field = value
            // TODO compute numOfTabs with vn (e.g. "Releases" tab may not exist if the VN has no releases, so numOfTabs depends on vn)
            numOfTabs = 5
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        val tab = Fragment()

        when (position) {
        }

        return tab
    }

    override fun getCount(): Int = numOfTabs
}