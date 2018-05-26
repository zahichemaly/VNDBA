package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.model.vndb.VN
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VNRepository @Inject constructor(var db: DB) : Repository<VN>() {
    override fun getItems(): Single<List<VN>> = Single.fromCallable {
        if (items.isNotEmpty()) items.values.toList()
        else db.vnDao().findAll()
    }

    override fun getItems(ids: List<Int>): Single<List<VN>> = Single.fromCallable {
        if (items.isNotEmpty()) items.filter { it.key in ids }.values.toList()
        else db.vnDao().findAll(ids)
    }

    override fun setItems(items: List<VN>): Completable = Completable.fromAction {
        this.items = items.map { it.id to it }.toMap().toMutableMap()
        db.vnDao().insertAll(items)
    }
}