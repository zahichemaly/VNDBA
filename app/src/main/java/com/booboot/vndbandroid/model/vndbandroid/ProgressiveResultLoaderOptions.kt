package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.adapter.vncards.Card
import com.booboot.vndbandroid.api.Cache
import com.booboot.vndbandroid.factory.ProgressiveResultLoader

class ProgressiveResultLoaderOptions {
    var cards: List<Card?> = emptyList()
    var currentPage: Int = 0
    var isMoreResults: Boolean = false

    fun isComplete(): Boolean {
        if (cards.isEmpty()) return false
        cards.filterNotNull()
                .map { it.vnId }
                .forEach { Cache.vns[it] ?: return false }
        return true
    }

    companion object {
        fun build(progressiveResultLoader: ProgressiveResultLoader?): ProgressiveResultLoaderOptions? {
            if (progressiveResultLoader == null) return null
            val options = ProgressiveResultLoaderOptions()
            options.cards = progressiveResultLoader.cards
            if (options.cards.isEmpty()) return null
            options.currentPage = progressiveResultLoader.currentPage
            options.isMoreResults = progressiveResultLoader.isMoreResults
            return options
        }
    }
}
