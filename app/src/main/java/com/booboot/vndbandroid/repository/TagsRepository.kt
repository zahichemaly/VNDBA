package com.booboot.vndbandroid.repository

import android.app.Application
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.extensions.saveToDisk
import com.booboot.vndbandroid.extensions.unzip
import com.booboot.vndbandroid.extensions.use
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.util.type
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TagsRepository @Inject constructor(
    var vndbService: VNDBService,
    var json: ObjectMapper,
    var app: Application
) : Repository<Tag>() {
    override fun getItems(cachePolicy: CachePolicy<Map<Int, Tag>>): Single<Map<Int, Tag>> = Single.fromCallable {
        cachePolicy
            .fetchFromMemory { items }
            .fetchFromDatabase { /* TODO */ items }
            .fetchFromNetwork {
                vndbService
                    .getTags()
                    .blockingGet()
                    .saveToDisk("tags.json.gz", app.cacheDir)
                    .unzip()
                    .use { json.readValue<List<Tag>>(it, type<List<Tag>>()).map { it.id to it }.toMap() }
                    ?: throw Exception("Error while getting tags")
            }
            .putInMemory { items = it.toMutableMap() }
            .putInDatabase { /* TODO */ }
            .isExpired { /* TODO */ false }
            .putExpiration { /* TODO */ }
            .get()
    }
}