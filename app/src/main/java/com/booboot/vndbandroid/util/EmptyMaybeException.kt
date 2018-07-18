package com.booboot.vndbandroid.util

/**
 * In an Rx flow, throw this exception to stop the next operators and just do nothing.
 */
class EmptyMaybeException : Throwable()