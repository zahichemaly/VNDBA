package com.booboot.vndbandroid.model.vndb

import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Tag(
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var meta: Boolean = false,
    var vns: Int = 0,
    var cat: String = "",
    var aliases: List<String> = emptyList(),
    var parents: List<Long> = emptyList()
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

    override fun hashCode() = id.toInt()

    companion object {
        const val KEY_GENRES = "genres"
        const val KEY_POPULAR = "popular"
        const val KEY_SEXUAL = "ero"
        const val KEY_CONTENT = "cont"
        const val KEY_TECHNICAL = "tech"

        val CATEGORIES = linkedMapOf(
            KEY_GENRES to R.string.genres,
            KEY_POPULAR to R.string.popular,
            KEY_CONTENT to R.string.content,
            KEY_TECHNICAL to R.string.technical,
            KEY_SEXUAL to R.string.sexual_content
        )

        fun checkSpoilerLevel(authorizedLevel: Int, actualLevel: Int): Boolean =
            if (authorizedLevel == 2) true else actualLevel < authorizedLevel + 1

        fun getScoreImage(tag: List<Number>): Int = tag[1].toFloat().let {
            when {
                it >= 2 -> R.drawable.rounded_green_background
                it >= 1 -> R.drawable.rounded_light_green_background
                it >= 0.5 -> R.drawable.rounded_yellow_background
                it >= 0.1 -> R.drawable.rounded_orange_background
                else -> R.drawable.rounded_red_background
            }
        }

        fun getCategoryName(category: String): String = App.context.getString(CATEGORIES[category] ?: R.string.content)
    }
}