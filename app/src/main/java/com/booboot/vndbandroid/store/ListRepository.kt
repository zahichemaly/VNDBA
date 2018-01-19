package com.booboot.vndbandroid.store

import com.booboot.vndbandroid.model.vndbandroid.AccountItem
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.realm.Realm

abstract class ListRepository<T : AccountItem> : Repository<T> {
    private var items = mutableMapOf<Int, T>()

    override fun add(item: T): Completable {
        items[item.vn] = item

        return Completable.create {
            Realm.getDefaultInstance().use {
                it.executeTransaction {
                    // TODO
                }
            }
        }
    }

    override fun get(itemId: Int): Maybe<T?> {
        if (items[itemId] != null)
            return Maybe.just(items[itemId])

        return Maybe.fromCallable {
            var item: T? = null

            Realm.getDefaultInstance().use {
                it.executeTransaction {
                    // TODO
                    if (item != null) items[itemId] = item
                }
            }

            item
        }
    }

    fun getIds(): Single<List<Int>> {
        return Single.fromCallable {
            emptyList<Int>()
        }
    }

    fun getItems(): Single<List<T>> {
        return Single.fromCallable {
            items.values.toList()
        }
    }

    fun setItems(items: List<T>): Completable {
        return Completable.create {
            this.items = items.map { it.vn to it }.toMap().toMutableMap()
        }
    }
}