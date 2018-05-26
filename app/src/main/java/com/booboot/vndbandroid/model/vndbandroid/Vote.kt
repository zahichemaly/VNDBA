package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R

import java.text.DecimalFormat
import java.util.regex.Pattern

object Vote {
    const val DEFAULT = "Add a vote"
    private val VOTE_FORMAT = DecimalFormat("#.#")

    fun outOf10(vote: Int) = vote / 10.0

    fun toString(vote: Int) =
            if (vote < 1) DEFAULT else toShortString(vote) + " (" + getName(outOf10(vote)) + ")"

    fun toShortString(vote: Int?): String = if (vote != null) VOTE_FORMAT.format(outOf10(vote)) else App.context.getString(R.string.dash)

    fun isValid(vote: String?): Boolean {
        if (vote == null) return false
        val pattern = Pattern.compile("[1-9](\\.[0-9])?")
        return pattern.matcher(vote).matches()
    }

    fun getName(vote: Double) = when {
        vote >= 10 -> "masterpiece"
        vote >= 9 -> "excellent"
        vote >= 8 -> "very good"
        vote >= 7 -> "good"
        vote >= 6 -> "decent"
        vote >= 5 -> "so-so"
        vote >= 4 -> "weak"
        vote >= 3 -> "bad"
        vote >= 2 -> "awful"
        vote >= 1 -> "worst ever"
        vote >= 0 -> "Other"
        else -> DEFAULT
    }

    fun getImage(vote: Double) = when {
        vote >= 8 -> R.drawable.score_green
        vote >= 7 -> R.drawable.score_light_green
        vote >= 6 -> R.drawable.score_yellow
        vote >= 4 -> R.drawable.score_light_orange
        vote >= 3 -> R.drawable.score_orange
        else -> R.drawable.score_red
    }
}
