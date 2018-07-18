package com.booboot.vndbandroid.ui.home

import android.app.Application
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.ui.base.StartupSyncViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class HomeViewModel constructor(application: Application) : StartupSyncViewModel(application) {
    init {
        (application as App).appComponent.inject(this)
    }

    fun startupSync() {
        if (disposables.contains(DISPOSABLE_STARTUP_SYNC)) return

        disposables[DISPOSABLE_STARTUP_SYNC] = startupSyncCompletable()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.value = true }
            .doFinally {
                loadingData.value = false
                disposables.remove(DISPOSABLE_STARTUP_SYNC)
            }
            .subscribe({
                accountData.value = it
                accountData.value = null
            }, ::onError)
    }

    companion object {
        private const val DISPOSABLE_STARTUP_SYNC = "DISPOSABLE_STARTUP_SYNC"
    }
}