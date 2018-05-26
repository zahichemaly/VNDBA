package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.model.vndbandroid.Priority
import com.booboot.vndbandroid.model.vndbandroid.Status

data class AccountItems(
        var vnlist: List<Vnlist> = emptyList(),
        var votelist: List<Votelist> = emptyList(),
        var wishlist: List<Wishlist> = emptyList(),
        var vns: List<VN> = emptyList()
) {
    fun getStatusCount() = mutableMapOf<Int, Int>().apply {
        Status.ALL.forEach { this[it] = 0 }
        vnlist.forEach {
            this[it.status] = this[it.status]?.plus(1) ?: 1
        }
    }

    fun getVoteCount() = mutableMapOf<Int, Int>().apply {
        for (i in 0 until 5) this[i] = 0
        votelist.forEach {
            when {
                it.vote >= 90 -> this[0] = this[0]?.plus(1) ?: 1
                it.vote >= 70 -> this[1] = this[1]?.plus(1) ?: 1
                it.vote >= 50 -> this[2] = this[2]?.plus(1) ?: 1
                it.vote >= 30 -> this[3] = this[3]?.plus(1) ?: 1
                else -> this[4] = this[4]?.plus(1) ?: 1
            }
        }
    }

    fun getWishCount() = mutableMapOf<Int, Int>().apply {
        Priority.ALL.forEach { this[it] = 0 }
        wishlist.forEach {
            this[it.priority] = this[it.priority]?.plus(1) ?: 1
        }
    }
}