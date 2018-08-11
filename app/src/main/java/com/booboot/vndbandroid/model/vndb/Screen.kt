package com.booboot.vndbandroid.model.vndb

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@SuppressWarnings(RoomWarnings.DEFAULT_CONSTRUCTOR)
@Entity(tableName = "screen", indices = [(Index("vnId"))]
//, foreignKeys = [
//    ForeignKey(entity = VN::class, parentColumns = arrayOf("id"), childColumns = arrayOf("vnId"), onDelete = CASCADE)]
)
data class Screen(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var vnId: Long = 0,
        var image: String = "",
        var rid: Int = 0,
        var nsfw: Boolean = false,
        var height: Int = 0,
        var width: Int = 0
)