package com.booboot.vndbandroid.ui.vntags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndbandroid.VNDetailsTags
import com.booboot.vndbandroid.model.vndbandroid.VNTag
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.tags_fragment_pager_item.view.*

internal class TagsPagerAdapter(context: Context, private val callback: (VNTag) -> Unit) : PagerAdapter() {
    private var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var tags: VNDetailsTags? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int = tags?.let { tags!!.all.keys.size } ?: 0

    override fun isViewFromObject(view: View, any: Any) = view === any

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.tags_fragment_pager_item, container, false)

        val flexbox = FlexboxLayoutManager(container.context)
        flexbox.alignItems = AlignItems.FLEX_START
        flexbox.justifyContent = JustifyContent.CENTER

        itemView.allList.layoutManager = flexbox
        val adapter = TagsAdapter(callback)
        itemView.allList.adapter = adapter
        adapter.items = tags?.all?.entries?.toList()?.get(position)?.value ?: emptyList()

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }

    override fun getPageTitle(position: Int): CharSequence? = Tag.getCategoryName(tags?.all?.keys?.toList()?.get(position) ?: "")
}