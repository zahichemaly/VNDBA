package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.R
import java.io.Serializable

data class Tag(
        var id: Int = 0,
        var name: String = "",
        var description: String = "",
        var meta: Boolean = false,
        var vns: Int = 0,
        var cat: String = "",
        var aliases: List<String> = emptyList(),
        var parents: List<Int> = emptyList()
) : Serializable {
    override fun toString(): String {
        return "Tag(name=$name)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    companion object {
        fun checkSpoilerLevel(authorizedLevel: Int, actualLevel: Int): Boolean =
                if (authorizedLevel == 2) true else actualLevel < authorizedLevel + 1

        fun getScoreImage(tag: List<Number>): Int = tag[1].toFloat().let {
            when {
                it >= 2 -> R.drawable.score_green
                it >= 1 -> R.drawable.score_light_green
                it >= 0 -> R.drawable.score_yellow
                else -> R.drawable.score_red
            }
        }
    }
}
