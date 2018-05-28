package com.booboot.vndbandroid.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.booboot.vndbandroid.model.vndb.Screen
import com.booboot.vndbandroid.model.vndb.VN

@Dao
abstract class VNDao {
    @Transaction
    @Query("SELECT * FROM vn WHERE id IN (:ids)")
    abstract fun _findAll(ids: List<Int>): List<VNWithRelations>

    @Transaction
    @Query("SELECT * FROM vn")
    abstract fun _findAll(): List<VNWithRelations>

    @Transaction
    @Query("SELECT * FROM vn WHERE id = :id LIMIT 1")
    abstract fun _find(id: Int): VNWithRelations?

    @Transaction
    @Insert(onConflict = REPLACE)
    abstract fun _insertAll(items: List<VN>)

    @Transaction
    @Insert(onConflict = REPLACE)
    abstract fun _insertScreens(screens: List<Screen>)

    @Transaction
    @Query("DELETE FROM vn")
    abstract fun deleteAll()

    fun findAll(ids: List<Int> = emptyList()): List<VN> {
        val vnWithRelations = if (ids.isEmpty()) _findAll() else _findAll(ids)
        return vnWithRelations.mapNotNull(::mapToVn)
    }

    fun find(id: Int): VN? = mapToVn(_find(id))

    private fun mapToVn(it: VNWithRelations?): VN? = it?.let {
        it.vn?.screens = it.screens ?: emptyList()
        it.vn
    }

    fun insertAll(vns: List<VN>) {
        vns.forEach {
            if (it.screens.isNotEmpty()) insertScreensForVn(it, it.screens)
        }
        _insertAll(vns)
    }

    fun insertScreensForVn(vn: VN, screens: List<Screen>) {
        screens.forEach { it.vnId = vn.id }
        _insertScreens(screens)
    }
}