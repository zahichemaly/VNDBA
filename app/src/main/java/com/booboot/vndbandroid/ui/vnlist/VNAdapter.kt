package com.booboot.vndbandroid.ui.vnlist

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.diff.VNDiffCallback
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN

/**
 * Created by od on 22/11/2016.
 */
class VNAdapter(
        private val onVnClicked: (View, VN) -> Unit,
        private val showFullDate: Boolean = false,
        private val showRank: Boolean = false,
        private val showRating: Boolean = false,
        private val showPopularity: Boolean = false,
        private val showVoteCount: Boolean = false
) : RecyclerView.Adapter<VNHolder>(), Filterable {
    var items = AccountItems()
        set(value) {
            field = value
            filter.filter(filterString)
        }
    private var filteredVns = items.vns
    private val mFilter = ItemFilter()
    private var filterString: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VNHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.vn_card, parent, false)
        return VNHolder(v, onVnClicked)
    }

    override fun onBindViewHolder(holder: VNHolder, position: Int) = holder.onBind(
            filteredVns[position],
            items.vnlist.find { it.vn == filteredVns[position].id },
            items.votelist.find { it.vn == filteredVns[position].id },
            items.wishlist.find { it.vn == filteredVns[position].id },
            showFullDate,
            showRank,
            showRating,
            showPopularity,
            showVoteCount
    )

    override fun getItemCount() = filteredVns.size

    override fun getFilter(): Filter = mFilter

    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
            filterString = constraint.toString().trim().toLowerCase()
            val results = Filter.FilterResults()
            val nlist = mutableListOf<VN>()

            items.vns.forEach {
                val filterableString = it.title
                if (filterableString.trim().toLowerCase().contains(filterString)) {
                    nlist.add(it)
                }
            }

            results.values = nlist
            results.count = nlist.size

            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            val newList = results.values as? List<VN> ?: emptyList()
            val diffResult = DiffUtil.calculateDiff(VNDiffCallback(filteredVns, newList))
            filteredVns = newList
            diffResult.dispatchUpdatesTo(this@VNAdapter)
        }
    }
}