package com.booboot.vndbandroid.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.booboot.vndbandroid.model.vndb.Screen
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.model.vndb.Wishlist

@Database(version = 1, exportSchema = false, entities = [
    Vnlist::class,
    Votelist::class,
    Wishlist::class,
    VN::class,
    Screen::class
])
@TypeConverters(value = [TagsTypeConverters::class, StringsTypeConverters::class, IntsTypeConverters::class])
abstract class DB : RoomDatabase() {
    abstract fun votelistDao(): VotelistDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun vnlistDao(): VnlistDao
    abstract fun vnDao(): VNDao
}