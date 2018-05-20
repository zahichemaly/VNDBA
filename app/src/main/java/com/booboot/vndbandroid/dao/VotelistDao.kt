package com.booboot.vndbandroid.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.booboot.vndbandroid.model.vndbandroid.Votelist

@Dao
interface VotelistDao {
    @Query("SELECT * FROM votelist")
    fun findAll(): List<Votelist>

    @Insert(onConflict = REPLACE)
    fun insertAll(items: List<Votelist>)

    @Query("DELETE FROM votelist")
    fun deleteAll()
}