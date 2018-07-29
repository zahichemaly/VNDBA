package com.booboot.vndbandroid.extensions

import com.booboot.vndbandroid.util.EmptyMaybeException
import io.reactivex.Maybe

fun <T> Maybe<T>.leaveIfEmpty(): Maybe<T> = doOnEvent { results: T?, throwable: Throwable? ->
    if (results == null && throwable == null) throw EmptyMaybeException()
}