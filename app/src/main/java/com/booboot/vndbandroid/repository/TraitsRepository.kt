package com.booboot.vndbandroid.repository

import android.app.Application
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.dao.TraitDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.extensions.saveToDisk
import com.booboot.vndbandroid.extensions.toBufferedSource
import com.booboot.vndbandroid.extensions.unzip
import com.booboot.vndbandroid.extensions.use
import com.booboot.vndbandroid.model.vndb.Trait
import com.booboot.vndbandroid.model.vndbandroid.Expiration
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.objectbox.BoxStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraitsRepository @Inject constructor(
    private var vndbService: VNDBService,
    var moshi: Moshi,
    var app: Application,
    var boxStore: BoxStore
) : Repository<Trait>() {
    override suspend fun getItems(coroutineScope: CoroutineScope, cachePolicy: CachePolicy<Map<Long, Trait>>): Deferred<Map<Long, Trait>> = coroutineScope.async(Dispatchers.IO) {
        cachePolicy
            .fetchFromMemory { items }
            .fetchFromDatabase {
                boxStore.get<TraitDao, Map<Long, Trait>> { it.all.map { it.toBo() }.associateBy { it.id } }
            }
            .fetchFromNetwork {
                vndbService
                    .getTraits()
                    .await()
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
                boxStore.save(true) { it.map { TraitDao(it.value, boxStore) } }
            }
            .isExpired { System.currentTimeMillis() > Expiration.traits }
            .putExpiration { Expiration.traits = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7 }
            .get()
    }
}