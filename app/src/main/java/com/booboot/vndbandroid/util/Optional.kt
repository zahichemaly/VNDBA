package com.booboot.vndbandroid.util

class Optional<T> {
    var value: T? = null

    private constructor() {
        this.value = null
    }

    private constructor(value: T?) {
        if (value != null)
            this.value = value
        else
            throw NullPointerException()
    }

    interface Action<T> {
        fun apply(value: T)
    }

    fun ifPresent(action: Action<T>) {
        value?.let { action.apply(value!!) }
    }

    fun empty() = value == null

    fun notEmpty() = value != null

    companion object {
        fun <T> empty(): Optional<T> {
            return Optional()
        }

        fun <T> of(value: T): Optional<T> {
            return Optional(value)
        }
    }
}