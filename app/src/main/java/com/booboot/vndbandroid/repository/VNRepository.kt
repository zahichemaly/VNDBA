package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.util.type
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VNRepository @Inject constructor(var db: DB, var vndbServer: VNDBServer) : Repository<VN>() {
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

    fun getItem(vnId: Int): Single<VN> = Single.fromCallable {
        if (items[vnId]?.screens?.isNotEmpty() == true) items[vnId]
        else {
            val completeVn: VN
            val dbVn = db.vnDao().find(vnId)
            if (dbVn?.screens?.isNotEmpty() == true) completeVn = dbVn
            else {
                val flags = if (dbVn != null) "screens" else "basic,details,stats,screens"
                val apiVn = vndbServer.get<VN>("vn", flags, "(id = $vnId)", Options(results = 1), type())
                        .blockingGet()
                        .items[0]

                dbVn?.screens = apiVn.screens
                completeVn = dbVn ?: apiVn
                db.vnDao().insertAll(listOf(completeVn))
            }

            items[vnId] = completeVn
            completeVn
        }
    }
}