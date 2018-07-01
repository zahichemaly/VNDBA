package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.model.vndb.Tag

/**
 * Complete representation of a tag for a given VN : the generic Tag as in tags.json, + VN-specific info (score, spoiler level).
 */
data class VNTag(
        val tag: Tag,
        val infos: ArrayList<Number>
)