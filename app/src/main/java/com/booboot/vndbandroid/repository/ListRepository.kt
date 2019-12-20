package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndb.AccountItem
import com.booboot.vndbandroid.model.vndb.Results

abstract class ListRepository<T : AccountItem> : Repository<T>() {
    protected abstract fun getItemsFromDB(): List<T>

    protected abstract fun addItemsToDB(items: List<T>)

    protected abstract suspend fun getItemsFromAPI(): Results<T>

    override suspend fun getItems(cachePolicy: CachePolicy<Map<Long, T>>) = cachePolicy
        .fetchFromMemory { items }
        .fetchFromDatabase { getItemsFromDB().associateBy { it.vn } }
        .fetchFromNetwork { getItemsFromAPI().items.associateBy { it.vn } }
        .putInMemory { if (cachePolicy.enabled) items = it.toMutableMap() }
        .putInDatabase { if (cachePolicy.enabled) addItemsToDB(it.values.toList()) }
        .get { emptyMap() }

    override suspend fun setItems(items: Map<Long, T>) {
        this@ListRepository.items = items.toMutableMap()
        addItemsToDB(items.values.toList())
    }
}