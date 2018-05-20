package com.booboot.vndbandroid.model.vndbandroid

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.RoomWarnings

@SuppressWarnings(RoomWarnings.DEFAULT_CONSTRUCTOR)
@Entity(tableName = "wishlist")
data class Wishlist(
        @PrimaryKey override var vn: Int = 0,
        override var added: Int = 0,
        var priority: Int = 0
) : AccountItem()