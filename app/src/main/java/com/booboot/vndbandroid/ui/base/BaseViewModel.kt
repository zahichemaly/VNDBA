package com.booboot.vndbandroid.ui.base

import android.app.Application
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.booboot.vndbandroid.extensions.errorMessage
import com.booboot.vndbandroid.extensions.log
import com.booboot.vndbandroid.extensions.minus
import com.booboot.vndbandroid.extensions.plus
import com.booboot.vndbandroid.util.EmptyMaybeException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel constructor(application: Application) : AndroidViewModel(application) {
    val loadingData = MutableLiveData<Int>()
    val errorData = MutableLiveData<String>()
    private val jobs = mutableMapOf<String, Job>()
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    /* State saving #2 : when coming back to the Fragment with the NavController (onSaveInstanceState not called) */
    var hasPendingTransition = false
    var layoutState: Parcelable? = null

    fun coroutine(jobName: String, skip: Boolean = false, errorHandler: CoroutineExceptionHandler = this.errorHandler, block: suspend CoroutineScope.() -> Unit) {
        if (skip || jobName in jobs) return

        loadingData.plus()
        jobs[jobName] = viewModelScope.launch(errorHandler) {
            block()
        }.apply {
            invokeOnCompletion {
                loadingData.minus()
                jobs.remove(jobName)
            }
        }
    }

    fun onError(throwable: Throwable) {
        onError(throwable, errorData)
    }

    fun onError(throwable: Throwable, errorData: MutableLiveData<String>) {
        if (throwable is EmptyMaybeException) return
        throwable.log()
        errorData.postValue(throwable.errorMessage())
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