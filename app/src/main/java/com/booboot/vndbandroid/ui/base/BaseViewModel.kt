package com.booboot.vndbandroid.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.booboot.vndbandroid.extensions.errorMessage
import com.booboot.vndbandroid.extensions.log
import com.booboot.vndbandroid.extensions.minus
import com.booboot.vndbandroid.extensions.plus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel constructor(application: Application) : AndroidViewModel(application) {
    val loadingData = MutableLiveData<Int>()
    val errorData = MutableLiveData<String>()
    private val jobs = mutableMapOf<String, Job>()
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    fun coroutine(jobName: String, skip: Boolean = false, errorHandler: CoroutineContext = this.errorHandler, block: suspend CoroutineScope.() -> Unit) {
        if (skip || jobName in jobs) return

        loadingData.plus()
        jobs[jobName] = viewModelScope.launch(Dispatchers.IO + errorHandler) {
            block()
        }.apply {
            invokeOnCompletion {
                viewModelScope.launch {
                    loadingData.minus()
                    jobs.remove(jobName)
                }
            }
        }
    }

    fun onError(throwable: Throwable) {
        onError(throwable, errorData)
    }

    fun onError(throwable: Throwable, errorData: MutableLiveData<String>) {
        throwable.log()
        errorData.postValue(throwable.errorMessage())
    }
}