package com.booboot.vndbandroid.repository

import io.reactivex.Completable
import io.reactivex.Single

abstract class Repository<T> {
    protected var items: MutableMap<Long, T> = mutableMapOf()

    open fun getItems(cachePolicy: CachePolicy<Map<Long, T>> = CachePolicy()): Single<Map<Long, T>> = Single.error(Throwable("not implemented"))
    open fun getItems(ids: List<Long>): Single<Map<Long, T>> = Single.error(Throwable("not implemented"))
    open fun getItem(id: Long, cachePolicy: CachePolicy<T> = CachePolicy()): Single<T> = Single.error(Throwable("not implemented"))
    open fun setItems(items: List<T>): Completable = Completable.error(Throwable("not implemented"))
}