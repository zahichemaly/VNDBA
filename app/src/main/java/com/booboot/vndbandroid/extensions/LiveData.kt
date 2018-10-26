package com.booboot.vndbandroid.extensions

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun MutableLiveData<Int>.plus() {
    value = value?.inc() ?: 1
}

fun MutableLiveData<Int>.minus() {
    value = if (value ?: 0 <= 1) 0 else value?.dec()
}

fun MutableLiveData<*>.reset() = Handler(Looper.getMainLooper()).post {
    value = null
}

fun <T> MutableLiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) = observe(lifecycleOwner, Observer<T> {
    it ?: return@Observer
    reset()
    action(it)
})