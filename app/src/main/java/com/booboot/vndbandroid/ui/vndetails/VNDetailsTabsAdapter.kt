package com.booboot.vndbandroid.ui.vndetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.vnsummary.SummaryFragment

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

        return when (position) {
            0 -> SummaryFragment()
            else -> Fragment()
        }
    }

    override fun getCount(): Int = numOfTabs
}