package com.booboot.vndbandroid.extensions

/**
 * Generates a unique Int with two Ints, based on the Cantor pairing function.
 * See https://stackoverflow.com/questions/919612/mapping-two-integers-to-one-in-a-unique-and-deterministic-way
 */
fun Long.generateUnique(b: Long) = (this + b) * (this + b + 1) / 2 + b