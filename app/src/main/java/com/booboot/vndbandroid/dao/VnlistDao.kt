package com.booboot.vndbandroid.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.booboot.vndbandroid.model.vndb.Vnlist

@Dao
abstract class VnlistDao {
    @Query("SELECT * FROM vnlist")
    abstract fun findAll(): List<Vnlist>

    @Insert(onConflict = REPLACE)
    protected abstract fun _insertAll(items: List<Vnlist>)

    @Query("DELETE FROM vnlist")
    abstract fun deleteAll()

    fun insertAll(items: List<Vnlist>) {
        deleteAll()
        _insertAll(items)
    }
}