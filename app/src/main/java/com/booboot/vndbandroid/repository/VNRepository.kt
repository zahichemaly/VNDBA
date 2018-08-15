package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.VNDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.util.type
import com.squareup.moshi.Moshi
import io.objectbox.BoxStore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VNRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer, var moshi: Moshi) : Repository<VN>() {
    override fun getItems(cachePolicy: CachePolicy<Map<Long, VN>>): Single<Map<Long, VN>> = Single.fromCallable {
        cachePolicy
            .fetchFromMemory { items }
            .fetchFromDatabase {
                boxStore.get<VNDao, Map<Long, VN>> { it.all.map { it.toBo() }.associateBy { it.id } }
            }
            .putInMemory { items.putAll(it) }
            .get()
    }

    override fun getItems(ids: List<Long>): Single<Map<Long, VN>> = Single.fromCallable {
        if (items.isEmpty()) {
            boxStore.get<VNDao, Map<Long, VN>> { it.get(ids).map { it.toBo() }.associateByTo(items) { it.id } }
        }
        items.filterKeys { it in ids }
    }

    override fun setItems(items: List<VN>): Completable = Completable.fromAction {
        items.associateByTo(this.items) { it.id }
        boxStore.save { items.map { VNDao(it, boxStore) } }
    }

    override fun getItem(id: Long, cachePolicy: CachePolicy<VN>): Single<VN> = Single.fromCallable {
        cachePolicy
            .fetchFromMemory { items[id] }
            .fetchFromDatabase { boxStore.get<VNDao, VN> { it.get(id).toBo() } }
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
            .putInDatabase { boxStore.save { listOf(VNDao(it, boxStore)) } }
            .get()
    }
}