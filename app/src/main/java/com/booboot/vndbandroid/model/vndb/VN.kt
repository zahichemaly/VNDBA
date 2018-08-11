package com.booboot.vndbandroid.model.vndb

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.booboot.vndbandroid.R
import com.squareup.moshi.JsonClass

@SuppressWarnings(RoomWarnings.DEFAULT_CONSTRUCTOR)
@Entity(tableName = "vn")
@JsonClass(generateAdapter = true)
data class VN(
    @PrimaryKey var id: Long = 0,
    var title: String = "",
    var original: String? = null,
    var released: String? = null,
    // TODO write a type converter for easy list to string conversion
    @Ignore var languages: List<String> = emptyList(),
    @Ignore var orig_lang: List<String> = emptyList(),
    @Ignore var platforms: List<String> = emptyList(),
    var aliases: String? = null,
    var length: Int? = 0,
    var description: String? = null,
    @Ignore var links: Links = Links(),
    var image: String? = null,
    var image_nsfw: Boolean = false,
    @Ignore var anime: List<Anime> = emptyList(),
    @Ignore var relations: List<VNRelation> = emptyList(),
    @JvmSuppressWildcards var tags: List<List<Float>> = emptyList(),
    var popularity: Float = 0f,
    var rating: Float = 0f,
    var votecount: Int = 0,
    @Ignore var screens: List<Screen> = emptyList(),
    @Ignore var staff: List<VnStaff> = emptyList()
) {
    fun lengthString(): String = when (length) {
        1 -> "Very short (< 2 hours)"
        2 -> "Short (2 - 10 hours)"
        3 -> "Medium (10 - 30 hours)"
        4 -> "Long (30 - 50 hours)"
        5 -> "Very long (> 50 hours)"
        else -> "Unknown"
    }

    fun lengthImage(): Int = when (length) {
        1 -> R.drawable.score_green
        2 -> R.drawable.score_light_green
        3 -> R.drawable.score_yellow
        4 -> R.drawable.score_orange
        5 -> R.drawable.score_red
        else -> -1
    }

    fun popularityImage(): Int = when {
        popularity >= 60 -> R.drawable.score_green
        popularity >= 40 -> R.drawable.score_light_green
        popularity >= 20 -> R.drawable.score_yellow
        popularity >= 10 -> R.drawable.score_light_orange
        popularity >= 1 -> R.drawable.score_orange
        else -> R.drawable.score_red
    }

    fun isComplete(): Boolean = screens.isNotEmpty() && tags.isNotEmpty()
}