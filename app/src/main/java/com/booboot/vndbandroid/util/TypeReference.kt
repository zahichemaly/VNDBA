package com.booboot.vndbandroid.util

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Generic class used to enclose a generics type, and easily retrieve it.
 */
abstract class TypeReference<T> protected constructor() : Comparable<TypeReference<T>> {
    val type: Type

    init {
        val superClass = javaClass.genericSuperclass
        if (superClass is Class<*>) { // sanity check, should never happen
            throw IllegalArgumentException("Internal error: TypeReference constructed without actual type information")
        }
        type = (superClass as ParameterizedType).actualTypeArguments[0]
    }

    override fun compareTo(other: TypeReference<T>): Int {
        return 0
    }
}

inline fun <reified T> type(): TypeReference<T> = object : TypeReference<T>() {}