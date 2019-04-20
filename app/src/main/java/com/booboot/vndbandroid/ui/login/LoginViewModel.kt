package com.booboot.vndbandroid.ui.login

import android.app.Application
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.ui.base.StartupSyncViewModel
import java.util.concurrent.atomic.AtomicInteger

class LoginViewModel constructor(application: Application) : StartupSyncViewModel(application) {
    val stepIndex: AtomicInteger = AtomicInteger(0)

    init {
        (application as App).appComponent.inject(this)
    }

    fun login() = coroutine(DISPOSABLE_LOGIN) {
        VNDBServer.closeAll().await()
        startupSync(this).await()
    }

    fun loginError() = errorData.value != null

    companion object {
        private const val DISPOSABLE_LOGIN = "DISPOSABLE_LOGIN"
    }
}