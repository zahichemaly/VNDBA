package com.booboot.vndbandroid.model.vndb

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.squareup.moshi.JsonClass

@SuppressWarnings(RoomWarnings.DEFAULT_CONSTRUCTOR)
@Entity(tableName = "wishlist")
@JsonClass(generateAdapter = true)
data class Wishlist(
        @PrimaryKey override var vn: Int = 0,
        override var added: Int = 0,
        var priority: Int = 0
) : AccountItem()