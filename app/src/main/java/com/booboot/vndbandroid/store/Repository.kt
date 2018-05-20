package com.booboot.vndbandroid.store

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface Repository<T> {
//    fun add(item: T): Completable
//    fun get(itemId: Int): Maybe<T?>

    fun getItems(): Single<List<T>>
    fun setItems(items: List<T>): Completable
}