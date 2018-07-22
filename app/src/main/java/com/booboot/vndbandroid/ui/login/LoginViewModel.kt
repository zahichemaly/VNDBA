package com.booboot.vndbandroid.ui.login

import android.app.Application
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.ui.base.StartupSyncViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel constructor(application: Application) : StartupSyncViewModel(application) {
    init {
        (application as App).appComponent.inject(this)
    }

    fun login() {
        if (disposables.contains(DISPOSABLE_LOGIN)) return

        disposables[DISPOSABLE_LOGIN] = VNDBServer.closeAll()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.value = true }
            .observeOn(Schedulers.io())
            .andThen(startupSyncCompletable())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loadingData.value = false
                disposables.remove(DISPOSABLE_LOGIN)
            }
            .subscribe({
                Preferences.loggedIn = true
                syncData.value = it
                syncData.value = null
            }, ::onError)
    }

    companion object {
        private const val DISPOSABLE_LOGIN = "DISPOSABLE_LOGIN"
    }
}