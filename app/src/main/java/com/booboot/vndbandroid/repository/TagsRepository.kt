package com.booboot.vndbandroid.repository

import android.app.Application
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.extensions.saveToDisk
import com.booboot.vndbandroid.extensions.toBufferedSource
import com.booboot.vndbandroid.extensions.unzip
import com.booboot.vndbandroid.extensions.use
import com.booboot.vndbandroid.model.vndb.Tag
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagsRepository @Inject constructor(
    var vndbService: VNDBService,
    var moshi: Moshi,
    var app: Application,
    var db: DB
) : Repository<Tag>() {
    override fun getItems(cachePolicy: CachePolicy<Map<Int, Tag>>): Single<Map<Int, Tag>> = Single.fromCallable {
        cachePolicy
            .fetchFromMemory { items }
            .fetchFromDatabase { /* TODO */ db.tagDao().findAll().associateBy { it.id } }
            .fetchFromNetwork {
                vndbService
                    .getTags()
                    .blockingGet()
                    .saveToDisk("tags.json.gz", app.cacheDir)
                    .unzip()
                    .use {
                        moshi
                            .adapter<List<Tag>>(Types.newParameterizedType(List::class.java, Tag::class.java))
                            .fromJson(it.toBufferedSource())
                            ?.associateBy { it.id }
                            ?: throw Exception("Error while getting tags : empty.")
                    }
            }
            .putInMemory { items = it.toMutableMap() }
            .putInDatabase { /* TODO */ db.tagDao().insertAll(it.values.toList()) }
            .isExpired { /* TODO */ false }
            .putExpiration { /* TODO */ }
            .get()
    }
}