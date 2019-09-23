package com.booboot.vndbandroid.ui.base

import android.widget.Filter

abstract class BaseFilter<T> : Filter() {
    @Volatile var currentFilters = 0

    abstract fun performFilter(search: CharSequence): T

    @Synchronized
    override fun performFiltering(search: CharSequence): FilterResults {
        currentFilters++
        return FilterResults().apply { values = performFilter(search) }
    }

    abstract fun publishResults(constraint: CharSequence, results: T)

    @Suppress("UNCHECKED_CAST")
    @Synchronized
    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        if (--currentFilters > 0) return
        val filterResults = results.values as? T ?: return
        publishResults(constraint, filterResults)
    }
}