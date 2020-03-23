package com.booboot.vndbandroid.ui.filters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.inflate
import com.booboot.vndbandroid.model.vndbandroid.FilterCheckbox
import com.booboot.vndbandroid.model.vndbandroid.FilterData
import com.booboot.vndbandroid.model.vndbandroid.FilterTitle
import com.booboot.vndbandroid.model.vndbandroid.SortItem
import com.booboot.vndbandroid.ui.base.BaseAdapter

class FiltersAdapter(
    private val onSortClicked: (SortItem) -> Unit
) : BaseAdapter<RecyclerView.ViewHolder>() {
    var data = FilterData()
        set(value) {
            field = value
            onUpdateInternal()
            value.diffResult?.dispatchUpdatesTo(this) ?: notifyChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int) = when (data.items[position]) {
        is FilterTitle -> FILTER_TITLE
        is FilterCheckbox -> FILTER_CHECKBOX
        is SortItem -> SORT_ITEM
        else -> FILTER_TITLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        FILTER_TITLE -> FilterTitleHolder(parent.inflate(R.layout.filter_title))
        FILTER_CHECKBOX -> FilterCheckboxHolder(parent.inflate(R.layout.filter_checkbox))
        else -> SortItemHolder(parent.inflate(R.layout.filter_button), onSortClicked)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data.items[position]
        when (holder) {
            is FilterTitleHolder -> holder.bind(item as FilterTitle)
            is FilterCheckboxHolder -> holder.bind(item as FilterCheckbox)
            is SortItemHolder -> holder.bind(item as SortItem)
        }
    }

    override fun getItemCount() = data.items.size

    override fun getItemId(position: Int) = data.items[position].id
}

const val FILTER_TITLE = 1
const val FILTER_CHECKBOX = 2
const val SORT_ITEM = 3