package com.booboot.vndbandroid.repository

import android.text.TextUtils
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.VNDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_BASIC
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_DETAILS
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_FULL
import com.booboot.vndbandroid.model.vndbandroid.FLAGS_NOT_EXISTS
import com.booboot.vndbandroid.util.type
import com.squareup.moshi.Moshi
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@Singleton
class VNRepository @Inject constructor(var boxStore: BoxStore, private var vndbServer: VNDBServer, var moshi: Moshi) : Repository<VN>() {
    override suspend fun getItems(cachePolicy: CachePolicy<Map<Long, VN>>) = cachePolicy
        .fetchFromMemory { items }
        .fetchFromDatabase {
            boxStore.get<VNDao, Map<Long, VN>> { it.all.map { vnDao -> vnDao.toBo() }.associateBy { vn -> vn.id } }
        }
        .putInMemory { items.putAll(it) }
        .get()

    override suspend fun getItems(ids: Set<Long>, flags: Int, cachePolicy: CachePolicy<Map<Long, VN>>) = cachePolicy
        .fetchFromMemory { items.filter { it.value.id in ids } }
        .fetchFromDatabase {
            boxStore.get<VNDao, Map<Long, VN>> { it.get(ids).map { vnDao -> vnDao.toBo(flags) }.associateBy { vn -> vn.id } }
        }
        .fetchFromNetwork {
            val dbVns = it?.toMutableMap() ?: mutableMapOf()
            val flagsList = linkedSetOf<String>()
            val newIds = linkedSetOf<Long>()

            ids.forEach { id ->
                val dbFlags = dbVns[id]?.flags ?: FLAGS_NOT_EXISTS
                if (FLAGS_BASIC in (dbFlags + 1)..flags) {
                    newIds.add(id)
                    flagsList.add("basic")
                }
                if (FLAGS_DETAILS in (dbFlags + 1)..flags) {
                    newIds.add(id)
                    flagsList.addAll(listOf("details", "stats"))
                }
                if (FLAGS_FULL in (dbFlags + 1)..flags) {
                    newIds.add(id)
                    flagsList.addAll(listOf("screens", "tags", "anime", "relations"))
                }
            }

            val mergedIdsString = TextUtils.join(",", newIds)
            val numberOfPages = ceil(newIds.size * 1.0 / 25).toInt()

            val apiVns = vndbServer.get<VN>("vn", TextUtils.join(",", flagsList), "(id = [$mergedIdsString])",
                Options(fetchAllPages = true, numberOfPages = numberOfPages), type())
                .items

            apiVns.forEach { apiVn ->
                val dbFlags = dbVns[apiVn.id]?.flags ?: FLAGS_NOT_EXISTS

                if (FLAGS_BASIC in (dbFlags + 1)..flags) {
                    dbVns[apiVn.id] = apiVn
                } else {
                    if (FLAGS_DETAILS in (dbFlags + 1)..flags) {
                        dbVns[apiVn.id]?.aliases = apiVn.aliases
                        dbVns[apiVn.id]?.length = apiVn.length
                        dbVns[apiVn.id]?.description = apiVn.description
                        dbVns[apiVn.id]?.links = apiVn.links
                        dbVns[apiVn.id]?.image = apiVn.image
                        dbVns[apiVn.id]?.image_nsfw = apiVn.image_nsfw
                        dbVns[apiVn.id]?.popularity = apiVn.popularity
                        dbVns[apiVn.id]?.rating = apiVn.rating
                        dbVns[apiVn.id]?.votecount = apiVn.votecount
                    }
                    if (FLAGS_FULL in (dbFlags + 1)..flags) {
                        dbVns[apiVn.id]?.screens = apiVn.screens
                        dbVns[apiVn.id]?.tags = apiVn.tags
                        dbVns[apiVn.id]?.anime = apiVn.anime
                        dbVns[apiVn.id]?.relations = apiVn.relations
                    }
                }

                dbVns[apiVn.id]?.flags = flags
            }
            dbVns
        }
        .isEmpty { cache ->
            ids.any {
                if (it !in cache) true
                else {
                    val dbFlags = cache[it]?.flags ?: FLAGS_NOT_EXISTS
                    FLAGS_BASIC in (dbFlags + 1)..flags ||
                        FLAGS_DETAILS in (dbFlags + 1)..flags ||
                        FLAGS_FULL in (dbFlags + 1)..flags
                }
            }
        }
        .putInMemory {
            if (cachePolicy.enabled) items.putAll(it.filter { item -> item.value.flags > items[item.key]?.flags ?: FLAGS_NOT_EXISTS })
        }
        .putInDatabase {
            if (cachePolicy.enabled) boxStore.save {
                it.mapNotNull {
                    if (it.value.flags > items[it.key]?.flags ?: FLAGS_NOT_EXISTS)
                        VNDao(it.value, boxStore)
                    else null
                }
            }
        }
        .get()

    override suspend fun setItems(items: Map<Long, VN>) {
        this@VNRepository.items.putAll(items)
        boxStore.save { items.map { VNDao(it.value, boxStore) } }
    }

    override suspend fun getItem(id: Long, cachePolicy: CachePolicy<VN>) =
        getItems(setOf(id), FLAGS_FULL, cachePolicy.copy())[id] ?: throw Throwable("VN not found.")

    suspend fun search(
        page: Int,
        query: String? = null,
        cachePolicy: CachePolicy<Map<Long, VN>> = CachePolicy(false)
    ) = cachePolicy
        .fetchFromNetwork {
            vndbServer.get<VN>("vn", "basic,details,stats", "(search ~ \"$query\")", Options(page = page), type())
                .items
                .associateBy { it.id }
        }
        .get()
}