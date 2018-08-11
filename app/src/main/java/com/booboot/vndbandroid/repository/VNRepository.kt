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
    override fun getItems(cachePolicy: CachePolicy<Map<Long, VN>>): Single<Map<Long, VN>> = Single.fromCallable {
        cachePolicy
            .fetchFromMemory { items }
            .fetchFromDatabase { db.vnDao().findAll().associateBy { it.id } }
            .putInMemory { items.putAll(it) }
            .get()
    }

    override fun getItems(ids: List<Long>): Single<Map<Long, VN>> = Single.fromCallable {
        if (items.isEmpty()) {
            db.vnDao().findAll(ids).associateByTo(items) { it.id }
        }
        items.filterKeys { it in ids }
    }

    override fun setItems(items: List<VN>): Completable = Completable.fromAction {
        items.associateByTo(this.items) { it.id }
        db.vnDao().insertAll(items)
    }

    override fun getItem(id: Long, cachePolicy: CachePolicy<VN>): Single<VN> = Single.fromCallable {
        cachePolicy
            .fetchFromMemory { items[id] }
            .fetchFromDatabase { db.vnDao().find(id) }
            .fetchFromNetwork { dbVn ->
                var flags = "screens,tags"
                if (dbVn == null) flags += ",basic,details,stats"

                val apiVn = vndbServer.get<VN>("vn", flags, "(id = $id)", Options(results = 1), type())
                    .blockingGet()
                    .items[0]

                dbVn?.screens = apiVn.screens
                dbVn?.tags = apiVn.tags
                dbVn ?: apiVn
            }
            .isEmpty { !it.isComplete() }
            .putInMemory { items[id] = it }
            .putInDatabase { db.vnDao().insertAll(listOf(it)) }
            .get()
    }
}