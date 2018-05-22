package com.booboot.vndbandroid.store

import io.reactivex.Completable
import io.reactivex.Single

abstract class Repository<T> {
    protected var items = mutableMapOf<Int, T>()

    abstract fun getItems(): Single<List<T>>
    abstract fun getItems(ids: List<Int>): Single<List<T>>
    abstract fun setItems(items: List<T>): Completable
}