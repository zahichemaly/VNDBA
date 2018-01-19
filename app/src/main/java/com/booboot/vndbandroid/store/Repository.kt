package com.booboot.vndbandroid.store

import io.reactivex.Completable
import io.reactivex.Maybe

interface Repository<T> {
    fun add(item: T): Completable
    fun get(itemId: Int): Maybe<T?>
}