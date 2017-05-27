package com.booboot.vndbandroid.model.vnstat

import com.booboot.vndbandroid.R
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class SimilarNovel(var novelId: Int = 0,
                   var similarity: Double = 0.toDouble(),
                   var title: String? = null,
                   var votecount: String? = null,
                   var popularity: String? = null,
                   var bayesianRating: String? = null,
                   var meanRating: String? = null,
                   var released: String? = null,
                   var image: String? = null,
                   var predictedRating: Double = 0.toDouble()) {

    companion object {
        const val IMAGE_LINK = "http://i.vnstat.net/"
    }

    fun getImageLink(): String = IMAGE_LINK + (image ?: "")

    fun getSimilarityPercentage(): Double = Math.floor(similarity * 1000 + 0.5) / 10

    fun getSimilarityImage(): Int = when {
        similarity >= 0.6 -> R.drawable.score_green
        similarity >= 0.4 -> R.drawable.score_light_green
        similarity >= 0.2 -> R.drawable.score_yellow
        similarity >= 0.1 -> R.drawable.score_light_orange
        similarity >= 0.01 -> R.drawable.score_orange
        else -> R.drawable.score_red
    }

    val predictedRatingPercentage: Double
        get() {
            val value = predictedRating * 10
            return Math.round(value * 100.0).toDouble() / 100.0
        }

    fun setNovelId(novelId: String) {
        this.novelId = Integer.parseInt(novelId)
    }

    fun setSimilarity(similarity: String) {
        this.similarity = similarity.toDouble()
    }
}
