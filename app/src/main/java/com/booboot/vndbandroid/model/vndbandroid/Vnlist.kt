package com.booboot.vndbandroid.model.vndbandroid

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.RoomWarnings

@SuppressWarnings(RoomWarnings.DEFAULT_CONSTRUCTOR)
@Entity(tableName = "vnlist")
data class Vnlist(
        @PrimaryKey override var vn: Int = 0,
        override var added: Int = 0,
        var status: Int = 0,
        var notes: String? = null
) : AccountItem()