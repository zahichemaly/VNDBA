package com.booboot.vndbandroid.ui.vntags

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Tag
import com.google.android.material.tabs.TabLayout
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.collapsing_tab.*
import kotlinx.android.synthetic.main.collapsing_tab.view.*

class TagTabHolder(
    override val containerView: View,
    private val onTitleClicked: (String) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer, TabLayout.OnTabSelectedListener {
    private lateinit var title: String
    private var collapsed: Boolean = false

    init {
        itemView.tabLayout.addOnTabSelectedListener(this)
    }

    fun onBind(_title: String, _collapsed: Boolean) {
        title = _title
        collapsed = _collapsed

        val fullTitle = Tag.getCategoryName(title)
        if (tabLayout.getTabAt(0)?.text != fullTitle) {
            tabLayout.removeAllTabs()
            tabLayout.addTab(tabLayout.newTab().setText(fullTitle).setCollapsedIcon())
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        collapsed = !collapsed
        tab.setCollapsedIcon()
        onTitleClicked(title)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
    }

    private fun TabLayout.Tab.setCollapsedIcon() = apply {
        setIcon(if (collapsed) R.drawable.ic_keyboard_arrow_down_white_24dp else R.drawable.ic_keyboard_arrow_up_white_24dp)
    }
}