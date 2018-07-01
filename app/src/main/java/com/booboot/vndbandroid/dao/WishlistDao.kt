package com.booboot.vndbandroid.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
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