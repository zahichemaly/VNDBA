package com.booboot.vndbandroid.dao

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.booboot.vndbandroid.model.vndb.Screen
import com.booboot.vndbandroid.model.vndb.VN

class VNWithRelations {
    @Embedded
    var vn: VN? = null

    @Relation(parentColumn = "id", entityColumn = "vnId", entity = Screen::class)
    var screens: List<Screen>? = null
}