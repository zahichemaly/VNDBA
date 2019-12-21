package com.booboot.vndbandroid.repository

import android.app.Application
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.dao.TagDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.extensions.saveToDisk
import com.booboot.vndbandroid.extensions.toBufferedSource
import com.booboot.vndbandroid.extensions.unzip
import com.booboot.vndbandroid.extensions.use
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndbandroid.Expiration
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagsRepository @Inject constructor(
    private var vndbService: VNDBService,
    var moshi: Moshi,
    var app: Application,
    var boxStore: BoxStore
) : Repository<Tag>() {
    override suspend fun getItems(cachePolicy: CachePolicy<Map<Long, Tag>>) = cachePolicy
        .fetchFromMemory { items }
        .fetchFromDatabase {
            boxStore.get<TagDao, Map<Long, Tag>> { it.all.map { tagDao -> tagDao.toBo() }.associateBy { tag -> tag.id } }
        }
        .fetchFromNetwork {
            vndbService
                .getTags()
                .saveToDisk("tags.json.gz", app.cacheDir)
                .unzip()
                .use {
                    moshi
                        .adapter<List<Tag>>(Types.newParameterizedType(List::class.java, Tag::class.java))
                        .fromJson(it.toBufferedSource())
                        ?.associateBy { tag -> tag.id }
                        ?: throw Exception("Error while getting tags : empty.")
                }
        }
        .putInMemory { items = it.toMutableMap() }
        .putInDatabase {
            boxStore.save(true) { it.map { TagDao(it.value, boxStore) } }
        }
        .isExpired { System.currentTimeMillis() > Expiration.tags }
        .putExpiration { Expiration.tags = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7 }
        .get()
}