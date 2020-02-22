package com.booboot.vndbandroid.model.vndbandroid

/**
 * Just a class to wrap results together, to be displayed in TagsFragment.
 */
data class VNDetailsTags(
    val all: MutableMap<String, List<VNTag>> = mutableMapOf()
) {
    fun copy(): VNDetailsTags {
        val res = VNDetailsTags()
        all.forEach {
            res.all[it.key] = it.value.toMutableList()
        }
        return res
    }

    fun get(position: Int): Any? {
        var i = 0
        all.forEach {
            if (i++ == position) return it.key
            it.value.forEach { vnTag ->
                if (i++ == position) return vnTag
            }
        }
        return null
    }

    fun count() = all.keys.size + all.values.sumBy { it.size }
}