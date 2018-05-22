package com.booboot.vndbandroid.extensions

import android.arch.persistence.room.RoomDatabase
import io.reactivex.Completable

fun <T : RoomDatabase> T.transaction(call: T.() -> Unit) = try {
    beginTransaction()
    call.invoke(this)
    setTransactionSuccessful()
} finally {
    endTransaction()
}

fun <T : RoomDatabase> T.completableTransaction(vararg actions: Completable): Completable =
        Completable.merge(actions.toMutableList())
                .doOnSubscribe { beginTransaction() }
                .doOnComplete { setTransactionSuccessful() }
                .doFinally { endTransaction() }