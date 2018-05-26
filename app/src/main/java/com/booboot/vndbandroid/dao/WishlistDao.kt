package com.booboot.vndbandroid.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.booboot.vndbandroid.model.vndb.Wishlist

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist")
    fun findAll(): List<Wishlist>

    @Insert(onConflict = REPLACE)
    fun insertAll(items: List<Wishlist>)

    @Query("DELETE FROM wishlist")
    fun deleteAll()
}