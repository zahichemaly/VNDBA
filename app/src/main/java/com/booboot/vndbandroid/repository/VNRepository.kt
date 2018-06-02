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
        else {
            val res = db.vnDao().findAll()
            this.items.putAll(res.map { it.id to it }.toMap().toMutableMap())
            res
        }
    }

    override fun getItems(ids: List<Int>): Single<List<VN>> = Single.fromCallable {
        if (items.isNotEmpty()) items.filter { it.key in ids }.values.toList()
        else {
            val res = db.vnDao().findAll(ids)
            this.items.putAll(res.map { it.id to it }.toMap().toMutableMap())
            res
        }
    }

    override fun setItems(items: List<VN>): Completable = Completable.fromAction {
        this.items = items.map { it.id to it }.toMap().toMutableMap()
        db.vnDao().insertAll(items)
    }

    fun getItem(vnId: Int): Single<VN> = Single.fromCallable {
        if (items[vnId]?.isComplete() == true) items[vnId]
        else {
            val completeVn: VN
            val dbVn = db.vnDao().find(vnId)
            if (dbVn?.isComplete() == true) completeVn = dbVn
            else {
                var flags = "screens,tags"
                if (dbVn == null) flags += "basic,details,stats"

                val apiVn = vndbServer.get<VN>("vn", flags, "(id = $vnId)", Options(results = 1), type())
                        .blockingGet()
                        .items[0]

                dbVn?.screens = apiVn.screens
                dbVn?.tags = apiVn.tags
                completeVn = dbVn ?: apiVn
                db.vnDao().insertAll(listOf(completeVn))
            }

            items[vnId] = completeVn
            completeVn
        }
    }
}