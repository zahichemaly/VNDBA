package com.booboot.vndbandroid.dao;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import com.booboot.vndbandroid.model.vndb.Screen;
import com.booboot.vndbandroid.model.vndb.VN;

import java.util.List;

public class VNWithRelations {
    @Embedded
    public VN vn;

    @Relation(parentColumn = "id", entityColumn = "vnId", entity = Screen.class)
    public List<Screen> screens;
}