package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.extensions.asyncOrLazy
import com.booboot.vndbandroid.model.vndb.AccountItem
import com.booboot.vndbandroid.model.vndb.Results
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

abstract class ListRepository<T : AccountItem> : Repository<T>() {
    protected abstract fun getItemsFromDB(): List<T>

    protected abstract fun addItemsToDB(items: List<T>)

    protected abstract suspend fun getItemsFromAPI(coroutineScope: CoroutineScope): Results<T>

    override suspend fun getItems(coroutineScope: CoroutineScope, cachePolicy: CachePolicy<Map<Long, T>>) = coroutineScope.async(Dispatchers.Default) {
        cachePolicy
            .fetchFromMemory { items }
            .fetchFromDatabase { getItemsFromDB().associateBy { it.vn } }
            .fetchFromNetwork { getItemsFromAPI(this).items.associateBy { it.vn } }
            .putInMemory { if (cachePolicy.enabled) items = it.toMutableMap() }
            .putInDatabase { if (cachePolicy.enabled) addItemsToDB(it.values.toList()) }
            .get { emptyMap() }
    }

    override suspend fun setItems(coroutineScope: CoroutineScope, items: Map<Long, T>, lazy: Boolean) = coroutineScope.asyncOrLazy(lazy) {
        this@ListRepository.items = items.toMutableMap()
        addItemsToDB(items.values.toList())
    }
}