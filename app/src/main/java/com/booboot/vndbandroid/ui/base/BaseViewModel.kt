package com.booboot.vndbandroid.ui.base

import android.app.Application
import android.os.Bundle
import android.os.Parcelable
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

    /* State saving #2 : when coming back to the Fragment with the NavController (onSaveInstanceState not called) */
    var hasPendingTransition = false
    var layoutState: Parcelable? = null

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
        layoutState = state.getParcelable(LAYOUT_STATE)
    }

    open fun saveState(state: Bundle) {
        /* State saving #3 : process kill (ViewModel and Fragment destroyed) */
        state.putBoolean(HAS_PENDING_TRANSITION, hasPendingTransition)
        state.putParcelable(LAYOUT_STATE, layoutState)
    }

    companion object {
        private const val HAS_PENDING_TRANSITION = "HAS_PENDING_TRANSITION"
        private const val LAYOUT_STATE = "LAYOUT_STATE"
    }
}