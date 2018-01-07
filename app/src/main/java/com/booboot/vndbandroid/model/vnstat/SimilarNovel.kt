package com.booboot.vndbandroid.model.vnstat

import com.booboot.vndbandroid.R

data class SimilarNovel(
        var novelId: Int = 0,
        var similarity: Float = 0f,
        var title: String = "",
        var votecount: String? = null,
        var popularity: String? = null,
        var bayesianRating: String? = null,
        var meanRating: String? = null,
        var released: String? = null,
        var image: String? = null,
        var predictedRating: Float = 0f
) {
    fun imageLink(): String = IMAGE_LINK + image

    fun similarityPercentage(): Double = Math.floor(similarity * 1000 + 0.5) / 10

    fun similarityImage(): Int = when {
        similarity >= 0.6 -> R.drawable.score_green
        similarity >= 0.4 -> R.drawable.score_light_green
        similarity >= 0.2 -> R.drawable.score_yellow
        similarity >= 0.1 -> R.drawable.score_light_orange
        similarity >= 0.01 -> R.drawable.score_orange
        else -> R.drawable.score_red
    }

    fun predictedRatingPercentage(): Double = Math.round(predictedRating * 10 * 100.0).toDouble() / 100.0

    fun setNovelId(novelId: String) {
        this.novelId = novelId.toInt()
    }

    fun setSimilarity(similarity: String) {
        this.similarity = similarity.toFloat()
    }

    companion object {
        val IMAGE_LINK = "http://i.vnstat.net/"
    }
}
