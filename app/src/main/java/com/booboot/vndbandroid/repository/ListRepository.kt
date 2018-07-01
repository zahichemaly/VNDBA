package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.model.vndb.AccountItem
import io.reactivex.Completable
import io.reactivex.Single

abstract class ListRepository<T : AccountItem> : Repository<T>() {
    abstract fun getItemsFromDB(): List<T>
    abstract fun addItemsToDB(items: List<T>)

    override fun getItems(): Single<List<T>> = Single.fromCallable {
        if (items.isNotEmpty()) items.values.toList()
        else getItemsFromDB()
    }

    override fun setItems(items: List<T>): Completable = Completable.fromAction {
        this.items = items.map { it.vn to it }.toMap().toMutableMap()
        addItemsToDB(items)
    }
}