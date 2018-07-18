package com.booboot.vndbandroid.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.BuildConfig
import com.booboot.vndbandroid.util.EmptyMaybeException
import io.reactivex.disposables.Disposable

open class BaseViewModel constructor(application: Application) : AndroidViewModel(application) {
    val loadingData: MutableLiveData<Boolean> = MutableLiveData()
    val errorData: MutableLiveData<String> = MutableLiveData()
    val disposables: MutableMap<String, Disposable> = mutableMapOf()

    fun onError(throwable: Throwable) {
        if (throwable is EmptyMaybeException) return
        if (BuildConfig.DEBUG) throwable.printStackTrace()
        errorData.value = throwable.localizedMessage
        errorData.value = null
    }
}