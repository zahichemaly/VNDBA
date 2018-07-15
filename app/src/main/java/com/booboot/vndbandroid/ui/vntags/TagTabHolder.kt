package com.booboot.vndbandroid.ui.vntags

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Tag
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.collapsing_tab.view.*

class TagTabHolder(itemView: View, private val callback: TagsAdapter.Callback) : RecyclerView.ViewHolder(itemView), TabLayout.OnTabSelectedListener {
    private lateinit var title: String
    private var collapsed: Boolean = false

    init {
        itemView.tabLayout.addOnTabSelectedListener(this)
    }

    fun onBind(title: String, collapsed: Boolean) = with(itemView) {
        this@TagTabHolder.title = title
        this@TagTabHolder.collapsed = collapsed

        val fullTitle = Tag.getCategoryName(title)
        if (tabLayout.getTabAt(0)?.text == fullTitle) {
            /* Trying to bind the same tab: nothing to do atm */
        } else {
            tabLayout.removeAllTabs()
            tabLayout.addTab(tabLayout.newTab().setText(fullTitle).setCollapsedIcon())
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        collapsed = !collapsed
        tab.setCollapsedIcon()
        callback.onTitleClicked(title)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
    }

    private fun TabLayout.Tab.setCollapsedIcon() = apply {
        setIcon(if (collapsed) R.drawable.ic_keyboard_arrow_down_white_24dp else R.drawable.ic_keyboard_arrow_up_white_24dp)
    }
}