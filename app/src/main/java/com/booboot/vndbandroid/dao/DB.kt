package com.booboot.vndbandroid.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.booboot.vndbandroid.model.vndbandroid.Vnlist
import com.booboot.vndbandroid.model.vndbandroid.Votelist
import com.booboot.vndbandroid.model.vndbandroid.Wishlist

@Database(version = 1, exportSchema = false, entities = [
    Vnlist::class,
    Votelist::class,
    Wishlist::class
])
abstract class DB : RoomDatabase() {
    abstract fun votelistDao(): VotelistDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun vnlistDao(): VnlistDao
}