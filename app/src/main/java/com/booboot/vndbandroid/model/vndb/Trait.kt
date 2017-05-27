package com.booboot.vndbandroid.model.vndb

import android.content.Context
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.util.JSON
import com.booboot.vndbandroid.util.Utils
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import java.io.IOException

@JsonIgnoreProperties(ignoreUnknown = true)
data class Trait(var parents: List<Int> = emptyList(),
                 var id: Int = 0,
                 var aliases: List<String> = emptyList(),
                 var chars: Int = 0,
                 var name: String = "",
                 var description: String = "",
                 var meta: Boolean = false) : VNDBCommand() {

    override fun toString(): String = name

    companion object {
        @Transient private var traits: MutableMap<Int, Trait> = mutableMapOf()

        fun getTraits(context: Context): Map<Int, Trait> {
            if (traits.isNotEmpty()) return traits

            val ins = context.resources.openRawResource(context.resources.getIdentifier("traits", "raw", context.packageName))
            try {
                val traitsList = JSON.mapper.readValue<List<Trait>>(ins, object : TypeReference<List<Trait>>() {})
                traitsList.forEach { traits[it.id] = it }
            } catch (e: IOException) {
                Utils.processException(e)
            }

            return traits
        }

        fun getScoreImage(tag: List<Number>): Int {
            val score = tag[1].toFloat()
            return when {
                score >= 2 -> R.drawable.score_green
                score >= 1 -> R.drawable.score_light_green
                score >= 0 -> R.drawable.score_yellow
                else -> R.drawable.score_red
            }
        }
    }
}
