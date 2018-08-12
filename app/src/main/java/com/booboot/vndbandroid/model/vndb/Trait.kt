package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.R
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Trait(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var meta: Boolean = false,
    var chars: Int = 0,
    var aliases: List<String> = emptyList(),
    var parents: List<Long> = emptyList()
) {
    override fun toString(): String {
        return "Trait(name=$name)"
    }

    companion object {
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