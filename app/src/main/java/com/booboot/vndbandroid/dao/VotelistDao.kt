package com.booboot.vndbandroid.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.booboot.vndbandroid.model.vndb.Votelist

@Dao
interface VotelistDao {
    @Query("SELECT * FROM votelist")
    fun findAll(): List<Votelist>

    @Insert(onConflict = REPLACE)
    fun insertAll(items: List<Votelist>)

    @Query("DELETE FROM votelist")
    fun deleteAll()
}