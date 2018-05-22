package com.booboot.vndbandroid.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.text.TextUtils
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.Votelist

@Dao
interface VNDao {
    @Query("SELECT * FROM vn WHERE id IN (:ids)")
    fun findAll(ids: List<Int>): List<VN>

    @Insert(onConflict = REPLACE)
    fun insertAll(items: List<VN>)

    @Query("DELETE FROM vn")
    fun deleteAll()
}