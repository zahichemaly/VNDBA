package com.booboot.vndbandroid.extensions

import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

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

suspend fun BoxStore.transaction(vararg actions: Deferred<*>): Unit = callInTx {
    runBlocking {
        actions.forEach { it.start() }
        actions.forEach { it.await() }
    }
}