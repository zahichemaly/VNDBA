package com.booboot.vndbandroid.model.vndb

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.squareup.moshi.JsonClass

@SuppressWarnings(RoomWarnings.DEFAULT_CONSTRUCTOR)
@Entity(tableName = "vnlist")
@JsonClass(generateAdapter = true)
data class Vnlist(
        @PrimaryKey override var vn: Int = 0,
        override var added: Int = 0,
        var status: Int = 0,
        var notes: String? = null
) : AccountItem()