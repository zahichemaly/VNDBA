package com.booboot.vndbandroid.dao

import androidx.room.TypeConverter
import com.booboot.vndbandroid.App
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

class TagsTypeConverters {
    @Inject lateinit var moshi: Moshi

    init {
        App.context.appComponent.inject(this)
    }

    @TypeConverter
    fun toJson(value: List<List<Float>>): String = moshi
        .adapter<List<List<Float>>>(Types.newParameterizedType(List::class.java, Types.newParameterizedType(List::class.java, java.lang.Float::class.java)))
        .toJson(value)

    @TypeConverter
    fun toTags(value: String): List<List<Float>> = moshi
        .adapter<List<List<Float>>>(Types.newParameterizedType(List::class.java, Types.newParameterizedType(List::class.java, java.lang.Float::class.java)))
        .fromJson(value)
        ?: emptyList()
}