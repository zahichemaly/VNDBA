package com.booboot.vndbandroid.ui.vntags

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Tag
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.collapsing_tab.view.*

class TagTabHolder(itemView: View, private val callback: TagsAdapter.Callback) : RecyclerView.ViewHolder(itemView), TabLayout.OnTabSelectedListener {
    private lateinit var title: String

    init {
        itemView.tabLayout.addOnTabSelectedListener(this)
    }

    fun onBind(title: String) = with(itemView) {
        this@TagTabHolder.title = title

        tabLayout.removeAllTabs()
        tabLayout.addTab(tabLayout.newTab().setText(Tag.getCategoryName(title)).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_white_24dp)))
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {
        callback.onTitleClicked(title)
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        callback.onTitleClicked(title)
    }
}