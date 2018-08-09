package com.booboot.vndbandroid.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.booboot.vndbandroid.model.vndb.Tag

@Dao
abstract class TagDao {
    @Query("SELECT * FROM tag")
    abstract fun findAll(): List<Tag>

    @Insert(onConflict = REPLACE)
    protected abstract fun _insertAll(items: List<Tag>)

    @Query("DELETE FROM tag")
    abstract fun deleteAll()

    fun insertAll(items: List<Tag>) {
        deleteAll()
        _insertAll(items)
    }
}