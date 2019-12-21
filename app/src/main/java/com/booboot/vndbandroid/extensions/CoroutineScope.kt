package com.booboot.vndbandroid.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

fun <T> CoroutineScope.asyncLazy(block: suspend CoroutineScope.() -> T) = async(Dispatchers.Unconfined, CoroutineStart.LAZY, block)

/**
 * Kotlin decided for some reason that CoroutineExceptionHandler didn't work for async blocks (see https://kotlinlang.org/docs/reference/coroutines/exception-handling.html#coroutineexceptionhandler).
 * So in order to execute an exception handler for an async block AND also its parent coroutine's exception handler, here's an extension
 * which catches any error thrown, executes a custom exception handler, and throw it back for its parent's exception handler.
 *
 * @param onError the exception handler = block to be executed in case of error.
 * @param block the usual async body with the work to be done asynchronously.
 */
fun <T> CoroutineScope.asyncWithError(onError: (Throwable) -> Unit, block: suspend CoroutineScope.() -> T) = async(Dispatchers.IO) {
    try {
        block()
    } catch (t: Throwable) {
        onError(t)
        throw t
    }
}

suspend fun <T> catchAndRethrow(onError: (Throwable) -> Unit, block: suspend () -> T) = try {
    block()
} catch (t: Throwable) {
    onError(t)
    throw t
}