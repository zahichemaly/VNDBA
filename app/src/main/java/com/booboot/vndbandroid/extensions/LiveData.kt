package com.booboot.vndbandroid.extensions

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Int>.plus() {
    value = value?.inc() ?: 1
}

fun MutableLiveData<Int>.minus() {
    value = if (value ?: 0 <= 1) 0 else value?.dec()
}