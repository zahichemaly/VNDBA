package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Label(
    var id: Long = 0,
    var label: String
) : Comparable<Label> {
    companion object {
        /* VNDB official labels */
        val UNKNOWN = Label(0L, "Unknown")
        val PLAYING = Label(1L, "Playing")
        val FINISHED = Label(2L, "Finished")
        val STALLED = Label(3L, "Stalled")
        val DROPPED = Label(4L, "Dropped")
        val WISHLIST = Label(5L, "Wishlist")
        val BLACKLIST = Label(6L, "Blacklist")
        val VOTED = Label(7L, "Voted")

        /* Custom labels */
        private const val VOTE_ID = -20100L
        val NO_VOTE = Label(-30100L, "No vote")
        const val NO_LABELS = -30101L
        const val CLEAR_FILTERS = -30102L
        val VOTES = mutableListOf<Label>().apply { for (i in 1..10) add(Label(VOTE_ID + i, "$i")) }

        /* Notable collections of label ids */
        val STATUSES = linkedSetOf(PLAYING.id, FINISHED.id, STALLED.id, DROPPED.id, UNKNOWN.id)
        val WISHLISTS = linkedSetOf(WISHLIST.id, BLACKLIST.id)
        val VOTELISTS = VOTES.map { it.id }
        val VOTE_CONTROLS = linkedSetOf(VOTED.id, NO_VOTE.id)
        val ALL_VOTES = VOTELISTS + VOTE_CONTROLS
        val ALL = STATUSES.plus(WISHLISTS)

        fun toShortString(status: Long?): String = when (status) {
            UNKNOWN.id -> "?"
            PLAYING.id -> "P"
            FINISHED.id -> "F"
            STALLED.id -> "S"
            DROPPED.id -> "D"
            WISHLIST.id -> "W"
            BLACKLIST.id -> "B"
            else -> App.context.getString(R.string.dash)
        }

        fun voteIdToVote(voteId: Long) = voteId - VOTE_ID
    }

    override fun compareTo(other: Label): Int {
        var index = ALL.indexOf(id).toLong()
        if (index < 0) index = id
        var otherIndex = ALL.indexOf(other.id).toLong()
        if (otherIndex < 0) otherIndex = other.id
        return index.compareTo(otherIndex)
    }
}