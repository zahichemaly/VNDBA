package com.booboot.vndbandroid.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
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