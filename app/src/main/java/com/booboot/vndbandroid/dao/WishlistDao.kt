package com.booboot.vndbandroid.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.booboot.vndbandroid.model.vndb.Wishlist

@Dao
abstract class WishlistDao {
    @Query("SELECT * FROM wishlist")
    abstract fun findAll(): List<Wishlist>

    @Insert(onConflict = REPLACE)
    protected abstract fun _insertAll(items: List<Wishlist>)

    @Query("DELETE FROM wishlist")
    abstract fun deleteAll()

    fun insertAll(items: List<Wishlist>) {
        deleteAll()
        _insertAll(items)
    }
}