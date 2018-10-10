package com.booboot.vndbandroid.ui.base

import android.app.Application
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

    fun onError(throwable: Throwable) {
        if (throwable is EmptyMaybeException) return
        throwable.log()

        errorData.value = throwable.errorMessage()
        errorData.value = null
    }
}