package com.booboot.vndbandroid.repository

import android.app.Application
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.dao.TraitDao
import com.booboot.vndbandroid.extensions.saveToDisk
import com.booboot.vndbandroid.extensions.toBufferedSource
import com.booboot.vndbandroid.extensions.unzip
import com.booboot.vndbandroid.extensions.use
import com.booboot.vndbandroid.model.vndb.Trait
import com.booboot.vndbandroid.model.vndbandroid.Expiration
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraitsRepository @Inject constructor(
    var vndbService: VNDBService,
    var moshi: Moshi,
    var app: Application,
    var boxStore: BoxStore
) : Repository<Trait>() {
    override fun getItems(cachePolicy: CachePolicy<Map<Long, Trait>>): Single<Map<Long, Trait>> = Single.fromCallable {
        cachePolicy
            .fetchFromMemory { items }
            .fetchFromDatabase {
                boxStore.callInReadTx {
                    boxStore.boxFor<TraitDao>().all.map { it.toBo() }.associateBy { it.id }
                }
            }
            .fetchFromNetwork {
                vndbService
                    .getTraits()
                    .blockingGet()
                    .saveToDisk("traits.json.gz", app.cacheDir)
                    .unzip()
                    .use {
                        moshi
                            .adapter<List<Trait>>(Types.newParameterizedType(List::class.java, Trait::class.java))
                            .fromJson(it.toBufferedSource())
                            ?.associateBy { it.id }
                            ?: throw Exception("Error while getting traits : empty.")
                    }
            }
            .putInMemory { items = it.toMutableMap() }
            .putInDatabase {
                boxStore.runInTx {
                    with(boxStore.boxFor<TraitDao>()) {
                        removeAll()
                        put(it.map { TraitDao(it.value, this) })
                    }
                }
            }
            .isExpired { System.currentTimeMillis() > Expiration.traits }
            .putExpiration { Expiration.traits = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7 }
            .get()
    }
}