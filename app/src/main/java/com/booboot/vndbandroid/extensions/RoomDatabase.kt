package com.booboot.vndbandroid.extensions

import androidx.room.RoomDatabase
import io.reactivex.Completable

fun <T : RoomDatabase> T.completableTransaction(vararg actions: Completable): Completable =
    Completable.merge(actions.toMutableList())
        .doOnSubscribe { beginTransaction() }
        .doOnComplete { setTransactionSuccessful() }
        .doFinally { endTransaction() }