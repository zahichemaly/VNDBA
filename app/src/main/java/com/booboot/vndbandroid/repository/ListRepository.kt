package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndb.AccountItem
import com.booboot.vndbandroid.model.vndb.Results
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

abstract class ListRepository<T : AccountItem> : Repository<T>() {
    protected abstract fun getItemsFromDB(): List<T>

    protected abstract fun addItemsToDB(items: List<T>)

    protected abstract fun getItemsFromAPI(): Results<T>

    override fun getItems(cachePolicy: CachePolicy<Map<Int, T>>): Single<Map<Int, T>> = Single.fromCallable {
        cachePolicy
            .fetchFromMemory { items }
            .fetchFromDatabase { getItemsFromDB().associateBy { it.vn } }
            .fetchFromNetwork { getItemsFromAPI().items.associateBy { it.vn } }
            .putInMemory { if (cachePolicy.enabled) items = it.toMutableMap() }
            .putInDatabase { if (cachePolicy.enabled) addItemsToDB(it.values.toList()) }
            .get { emptyMap() }
    }.subscribeOn(Schedulers.newThread())

    override fun setItems(items: List<T>): Completable = Completable.fromAction {
        this.items = items.associateBy { it.vn }.toMutableMap()
        addItemsToDB(items)
    }
}