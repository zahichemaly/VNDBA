package com.booboot.vndbandroid.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.booboot.vndbandroid.model.vndb.*

@Database(version = 1, exportSchema = false, entities = [
    Vnlist::class,
    Votelist::class,
    Wishlist::class,
    VN::class,
    Screen::class
])
@TypeConverters(value = [TagsTypeConverters::class])
abstract class DB : RoomDatabase() {
    abstract fun votelistDao(): VotelistDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun vnlistDao(): VnlistDao
    abstract fun vnDao(): VNDao
}