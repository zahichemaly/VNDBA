package com.booboot.vndbandroid.dao

import androidx.room.TypeConverter
import com.booboot.vndbandroid.di.JSONModule
import com.booboot.vndbandroid.util.type

class TagsTypeConverters {
    @TypeConverter
    fun toJson(value: ArrayList<ArrayList<Number>>): String = JSONModule.objectMapper().writeValueAsString(value)

    @TypeConverter
    fun toTags(value: String): ArrayList<ArrayList<Number>> = JSONModule.objectMapper().readValue(value, type<List<List<Number>>>())
}