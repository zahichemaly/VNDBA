package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndb.AccountItem
import io.reactivex.Completable
import io.reactivex.Single

abstract class ListRepository<T : AccountItem> : Repository<T>() {
    abstract fun getItemsFromDB(): List<T>
    abstract fun addItemsToDB(items: List<T>)

    override fun getItems(cachePolicy: CachePolicy<Map<Int, T>>): Single<Map<Int, T>> = Single.fromCallable {
        cachePolicy
            .fetchFromMemory { items }
            .fetchFromDatabase { getItemsFromDB().map { it.vn to it }.toMap() }
            .putInMemory { items = it.toMutableMap() }
            .get()
    }

    override fun setItems(items: List<T>): Completable = Completable.fromAction {
        this.items = items.map { it.vn to it }.toMap().toMutableMap()
        addItemsToDB(items)
    }
}