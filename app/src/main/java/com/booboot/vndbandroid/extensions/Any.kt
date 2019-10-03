package com.booboot.vndbandroid.extensions

fun <T> tryOrDefault(defaultValue: T, f: () -> T) = try {
    f()
} catch (e: Exception) {
    defaultValue
}

fun <T> tryOrNull(f: () -> T) = tryOrDefault(null, f)