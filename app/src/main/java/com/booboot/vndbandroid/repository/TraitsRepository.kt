package com.booboot.vndbandroid.repository

import android.app.Application
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.dao.TraitDao
import com.booboot.vndbandroid.di.BoxManager
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraitsRepository @Inject constructor(
    private var vndbService: VNDBService,
    var moshi: Moshi,
    var app: Application,
    var boxManager: BoxManager
) : Repository<Trait>() {
    override suspend fun getItems(cachePolicy: CachePolicy<Map<Long, Trait>>) = cachePolicy
        .fetchFromMemory { items }
        .fetchFromDatabase {
            boxManager.boxStore.get<TraitDao, Map<Long, Trait>> { it.all.map { traitDao -> traitDao.toBo() }.associateBy { trait -> trait.id } }
        }
        .fetchFromNetwork {
            vndbService
                .getTraits()
                .saveToDisk("traits.json.gz", app.cacheDir)
                .unzip()
                .use {
                    moshi
                        .adapter<List<Trait>>(Types.newParameterizedType(List::class.java, Trait::class.java))
                        .fromJson(it.toBufferedSource())
                        ?.associateBy { trait -> trait.id }
                        ?: throw Exception("Error while getting traits : empty.")
                }
        }
        .putInMemory { items = it.toMutableMap() }
        .putInDatabase {
            boxManager.boxStore.save(true) { it.map { TraitDao(it.value, boxManager.boxStore) } }
        }
        .isExpired { System.currentTimeMillis() > Expiration.traits }
        .putExpiration { Expiration.traits = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7 }
        .get()
}