package com.booboot.vndbandroid.ui.login

import android.app.Application
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.extensions.minus
import com.booboot.vndbandroid.extensions.plus
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.ui.base.StartupSyncViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicInteger

class LoginViewModel constructor(application: Application) : StartupSyncViewModel(application) {
    val stepIndex: AtomicInteger = AtomicInteger(0)

    init {
        (application as App).appComponent.inject(this)
    }

    fun login() {
        if (disposables.contains(DISPOSABLE_LOGIN)) return

        disposables[DISPOSABLE_LOGIN] = VNDBServer.closeAll()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.plus() }
            .observeOn(Schedulers.io())
            .andThen(startupSyncSingle())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loadingData.minus()
                disposables.remove(DISPOSABLE_LOGIN)
            }
            .subscribe({ Preferences.loggedIn = true }, ::onError)
    }

    fun loginError() = errorData.value != null

    companion object {
        private const val DISPOSABLE_LOGIN = "DISPOSABLE_LOGIN"
    }
}