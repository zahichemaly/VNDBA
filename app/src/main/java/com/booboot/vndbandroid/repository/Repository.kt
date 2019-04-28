package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndbandroid.ApiFlags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

abstract class Repository<T> {
    protected var items: MutableMap<Long, T> = mutableMapOf()

    open suspend fun getItems(coroutineScope: CoroutineScope, cachePolicy: CachePolicy<Map<Long, T>> = CachePolicy()): Deferred<Map<Long, T>> = throw Throwable("not implemented")
    open suspend fun getItems(coroutineScope: CoroutineScope, ids: Set<Long>, @ApiFlags flags: Int, cachePolicy: CachePolicy<Map<Long, T>> = CachePolicy()): Deferred<Map<Long, T>> = throw Throwable("not implemented")
    open suspend fun getItem(coroutineScope: CoroutineScope, id: Long, cachePolicy: CachePolicy<T> = CachePolicy()): Deferred<T> = throw Throwable("not implemented")
    open suspend fun setItems(coroutineScope: CoroutineScope, items: Map<Long, T>, lazy: Boolean = false): Deferred<*> = throw Throwable("not implemented")
}