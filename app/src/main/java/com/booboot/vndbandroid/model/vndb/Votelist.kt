package com.booboot.vndbandroid.model.vndb

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.squareup.moshi.JsonClass

@SuppressWarnings(RoomWarnings.DEFAULT_CONSTRUCTOR)
@Entity(tableName = "votelist")
@JsonClass(generateAdapter = true)
data class Votelist(
    @PrimaryKey override var vn: Long = 0,
    override var added: Int = 0,
    var vote: Int = 0
) : AccountItem()