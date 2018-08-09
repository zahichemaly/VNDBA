package com.booboot.vndbandroid.dao

import androidx.room.TypeConverter
import com.booboot.vndbandroid.App
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

class IntsTypeConverters {
    @Inject lateinit var moshi: Moshi

    init {
        App.context.appComponent.inject(this)
    }

    @TypeConverter
    fun toJson(value: List<Int>): String = moshi
        .adapter<List<Int>>(Types.newParameterizedType(List::class.java, Integer::class.java))
        .toJson(value)

    @TypeConverter
    fun fromJson(value: String): List<Int> = moshi
        .adapter<List<Int>>(Types.newParameterizedType(List::class.java, Integer::class.java))
        .fromJson(value)
        ?: emptyList()
}