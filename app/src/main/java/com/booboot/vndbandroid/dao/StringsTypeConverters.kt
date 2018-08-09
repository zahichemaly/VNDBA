package com.booboot.vndbandroid.dao

import androidx.room.TypeConverter
import com.booboot.vndbandroid.App
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

class StringsTypeConverters {
    @Inject lateinit var moshi: Moshi

    init {
        App.context.appComponent.inject(this)
    }

    @TypeConverter
    fun toJson(value: List<String>): String = moshi
        .adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
        .toJson(value)

    @TypeConverter
    fun fromJson(value: String): List<String> = moshi
        .adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
        .fromJson(value)
        ?: emptyList()
}