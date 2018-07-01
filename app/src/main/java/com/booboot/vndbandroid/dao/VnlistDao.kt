package com.booboot.vndbandroid.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.booboot.vndbandroid.model.vndb.Vnlist

@Dao
interface VnlistDao {
    @Query("SELECT * FROM vnlist")
    fun findAll(): List<Vnlist>

    @Insert(onConflict = REPLACE)
    fun insertAll(items: List<Vnlist>)

    @Query("DELETE FROM vnlist")
    fun deleteAll()
}