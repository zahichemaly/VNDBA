package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndb.Trait
import com.squareup.moshi.Moshi
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraitsRepository @Inject constructor(var moshi: Moshi) : Repository<Trait>() {
    override fun getItems(cachePolicy: CachePolicy<Map<Long, Trait>>): Single<Map<Long, Trait>> = Single.fromCallable {
        items
        // TODO
    }
}