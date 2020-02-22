package com.booboot.vndbandroid.model.vndbandroid

class Sections<T>(
    val all: MutableMap<String, List<T>> = mutableMapOf()
) {
    fun copy(): Sections<T> {
        val res = Sections<T>()
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