package com.booboot.vndbandroid.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.booboot.vndbandroid.model.vndb.Votelist

@Dao
abstract class VotelistDao {
    @Query("SELECT * FROM votelist")
    abstract fun findAll(): List<Votelist>

    @Insert(onConflict = REPLACE)
    protected abstract fun _insertAll(items: List<Votelist>)

    @Query("DELETE FROM votelist")
    abstract fun deleteAll()

    fun insertAll(items: List<Votelist>) {
        deleteAll()
        _insertAll(items)
    }
}