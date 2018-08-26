package com.booboot.vndbandroid.model.vndb

import androidx.annotation.ColorRes
import com.booboot.vndbandroid.R
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VN(
    var id: Long = 0,
    var title: String = "",
    var original: String? = null,
    var released: String? = null,
    var languages: List<String> = emptyList(),
    var orig_lang: List<String> = emptyList(),
    var platforms: List<String> = emptyList(),
    var aliases: String? = null,
    var length: Int? = 0,
    var description: String? = null,
    var links: Links = Links(),
    var image: String? = null,
    var image_nsfw: Boolean = false,
    var anime: List<Anime> = emptyList(),
    var relations: List<Relation> = emptyList(),
    @JvmSuppressWildcards var tags: List<List<Float>> = emptyList(),
    var popularity: Float = 0f,
    var rating: Float = 0f,
    var votecount: Int = 0,
    var screens: List<Screen> = emptyList(),
    var staff: List<VnStaff> = emptyList()
) {
    fun lengthFull(): String = lengthString() + " (" + lengthInHours() + ")"

    fun lengthString(): String = when (length) {
        1 -> "Very short"
        2 -> "Short"
        3 -> "Medium"
        4 -> "Long"
        5 -> "Very long"
        else -> "Unknown"
    }

    fun lengthInHours(): String = when (length) {
        1 -> "< 2 hours"
        2 -> "2 - 10 hours"
        3 -> "10 - 30 hours"
        4 -> "30 - 50 hours"
        5 -> "> 50 hours"
        else -> "Unknown"
    }

    @ColorRes
    fun popularityColor(): Int = when {
        popularity >= 60 -> R.color.green
        popularity >= 40 -> R.color.lightGreen
        popularity >= 20 -> R.color.yellow
        popularity >= 10 -> R.color.lightOrange
        popularity >= 1 -> R.color.orange
        else -> R.color.red
    }

    fun isComplete(): Boolean = screens.isNotEmpty() || tags.isNotEmpty() || relations.isNotEmpty()
}