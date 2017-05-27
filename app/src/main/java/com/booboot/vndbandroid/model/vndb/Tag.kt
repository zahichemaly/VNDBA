package com.booboot.vndbandroid.model.vndb

import android.content.Context
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.activity.VNDetailsActivity
import com.booboot.vndbandroid.util.JSON
import com.booboot.vndbandroid.util.Utils
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import java.io.IOException

@JsonIgnoreProperties(ignoreUnknown = true)
data class Tag(var parents: List<Int> = emptyList(),
               var id: Int = 0,
               var aliases: List<String> = emptyList(),
               var vns: Int = 0,
               var cat: String = "",
               var name: String = "",
               var description: String = "",
               var meta: Boolean = false) : VNDBCommand() {

    companion object {
        @Transient private var tags: MutableMap<Int, Tag> = mutableMapOf()

        fun getTags(context: Context): Map<Int, Tag> {
            if (tags.isNotEmpty()) return tags

            val ins = context.resources.openRawResource(context.resources.getIdentifier("tags", "raw", context.packageName))
            try {
                val tagsList = JSON.mapper.readValue<List<Tag>>(ins, object : TypeReference<List<Tag>>() {})
                tagsList.forEach { tags[it.id] = it }
            } catch (e: IOException) {
                Utils.processException(e)
            }

            return tags
        }

        fun getTagsArray(context: Context): Array<Tag> = getTags(context).values.toTypedArray()

        fun checkSpoilerLevel(activity: VNDetailsActivity, level: Int): Boolean = when {
            activity.spoilerLevel == 2 -> true
            else -> level < activity.spoilerLevel + 1
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

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Tag) return false
        return id == other.id
    }

    override fun hashCode(): Int = id
}