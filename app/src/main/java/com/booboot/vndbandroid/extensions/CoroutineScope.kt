package com.booboot.vndbandroid.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

fun <T> CoroutineScope.asyncOrLazy(lazy: Boolean = false, block: suspend CoroutineScope.() -> T) =
    if (lazy) async(Dispatchers.Unconfined, CoroutineStart.LAZY, block)
    else async(Dispatchers.IO, block = block)