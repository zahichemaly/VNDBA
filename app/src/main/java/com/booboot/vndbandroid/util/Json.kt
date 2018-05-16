package com.booboot.vndbandroid.util

import com.fasterxml.jackson.core.type.TypeReference

inline fun <reified T> type(): TypeReference<T> = object : TypeReference<T>() {}