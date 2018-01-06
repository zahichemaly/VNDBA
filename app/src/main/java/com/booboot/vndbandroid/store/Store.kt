package com.booboot.vndbandroid.store

import io.reactivex.Completable
import io.reactivex.Maybe

interface Store<T> {
    fun add(restaurant: T): Completable
    fun get(restaurantId: Int): Maybe<T?>
}