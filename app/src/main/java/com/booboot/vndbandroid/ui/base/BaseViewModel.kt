package com.booboot.vndbandroid.ui.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.extensions.errorMessage
import com.booboot.vndbandroid.extensions.log
import com.booboot.vndbandroid.util.EmptyMaybeException
import io.reactivex.disposables.Disposable

abstract class BaseViewModel constructor(application: Application) : AndroidViewModel(application) {
    val loadingData: MutableLiveData<Int> = MutableLiveData()
    val errorData: MutableLiveData<String> = MutableLiveData()
    val disposables: MutableMap<String, Disposable> = mutableMapOf()
    var hasPendingTransition = false

    fun onError(throwable: Throwable) {
        onError(throwable, errorData)
    }

    fun onError(throwable: Throwable, errorData: MutableLiveData<String>) {
        if (throwable is EmptyMaybeException) return
        throwable.log()
        errorData.value = throwable.errorMessage()
    }

    override fun onCleared() {
        disposables.forEach { it.value.dispose() }
    }

    open fun restoreState(state: Bundle?) {
        state ?: return
        hasPendingTransition = state.getBoolean(HAS_PENDING_TRANSITION)
    }

    open fun saveState(state: Bundle) {
        state.putBoolean(HAS_PENDING_TRANSITION, hasPendingTransition)
    }

    companion object {
        private const val HAS_PENDING_TRANSITION = "HAS_PENDING_TRANSITION"
    }
}