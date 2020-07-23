package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndbandroid.ApiFlags

abstract class Repository<T> {
    protected var items: MutableMap<Long, T> = mutableMapOf()

    open suspend fun getItems(cachePolicy: CachePolicy<Map<Long, T>> = CachePolicy()): Map<Long, T> = throw Throwable("not implemented")
    open suspend fun getItems(ids: Set<Long>, @ApiFlags flags: Int, cachePolicy: CachePolicy<Map<Long, T>> = CachePolicy()): Map<Long, T> = throw Throwable("not implemented")
    open suspend fun getItem(id: Long, cachePolicy: CachePolicy<T> = CachePolicy()): T = throw Throwable("not implemented")
    open suspend fun setItems(items: Map<Long, T>) {
        throw Throwable("not implemented")
    }

    fun clear() {
        items.clear()
    }
}