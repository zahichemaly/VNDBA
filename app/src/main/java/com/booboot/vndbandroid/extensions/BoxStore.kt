package com.booboot.vndbandroid.extensions

import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.reactivex.Completable

inline fun <reified T> BoxStore.save(removeAllFirst: Boolean = false, crossinline items: () -> List<T>) = runInTx {
    with(boxFor<T>()) {
        if (removeAllFirst) removeAll()
        put(items())
    }
}

inline fun <reified T> BoxStore.save(crossinline items: () -> List<T>) = save(false, items)

inline fun <reified T, U> BoxStore.get(crossinline action: (Box<T>) -> U): U = callInReadTx {
    action(boxFor())
}

fun BoxStore.completableTransaction(vararg actions: Completable): Completable = Completable.fromAction {
    callInTx {
        Completable.merge(actions.toMutableList()).blockingAwait()
    }
}