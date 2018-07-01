package com.booboot.vndbandroid.model.vndbandroid

/**
 * Just a class to wrap results together, to be displayed in TagsFragment.
 */
data class VNDetailsTags(
        val all: Map<String, List<VNTag>> = emptyMap()
)