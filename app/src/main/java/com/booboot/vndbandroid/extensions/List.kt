package com.booboot.vndbandroid.extensions

fun <T, U> MutableMap<T, MutableList<U>>.addOrCreate(key: T, value: U) {
    this[key] = this[key] ?: mutableListOf()
    this[key]?.add(value)
}

inline fun <T> Collection<T>.emptyOrAny(predicate: (T) -> Boolean) = isEmpty() || any(predicate)