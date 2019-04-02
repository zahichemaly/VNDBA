package com.booboot.vndbandroid.ui.vndetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.ui.vnrelations.RelationsFragment
import com.booboot.vndbandroid.ui.vnsummary.SummaryFragment
import com.booboot.vndbandroid.ui.vntags.TagsFragment

class VNDetailsTabsAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private var numOfTabs = 0
    var vn: VN? = null
        set(value) {
            field = value
            // TODO compute numOfTabs with vn (e.g. "Releases" tab may not exist if the VN has no releases, so numOfTabs depends on vn)
            numOfTabs = tabs.size
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment = getClass(position).newInstance().apply {
        arguments = Bundle().apply {
            putLong(VNDetailsFragment.EXTRA_VN_ID, vn?.id ?: 0)
        }
    }

    override fun getCount(): Int = numOfTabs

    private val tabs = listOf("Summary", "Tags", "Relations", "Characters", "Releases", "Staff")
    override fun getPageTitle(position: Int): CharSequence? = tabs[position]

    fun getClass(position: Int) = when (position) {
        0 -> SummaryFragment::class.java
        1 -> TagsFragment::class.java
        2 -> RelationsFragment::class.java
        else -> Fragment::class.java
    }
}