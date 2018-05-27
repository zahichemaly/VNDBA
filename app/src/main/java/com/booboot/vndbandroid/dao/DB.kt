package com.booboot.vndbandroid.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.booboot.vndbandroid.model.vndb.*

@Database(version = 1, exportSchema = false, entities = [
    Vnlist::class,
    Votelist::class,
    Wishlist::class,
    VN::class,
    Screen::class
])
abstract class DB : RoomDatabase() {
    abstract fun votelistDao(): VotelistDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun vnlistDao(): VnlistDao
    abstract fun vnDao(): VNDao
}