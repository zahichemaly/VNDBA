package com.booboot.vndbandroid.ui.login

import android.app.Application
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.ui.base.StartupSyncViewModel

class LoginViewModel constructor(application: Application) : StartupSyncViewModel(application) {
    init {
        (application as App).appComponent.inject(this)
    }

    fun login() = coroutine(DISPOSABLE_LOGIN) {
        VNDBServer.closeAll()
        startupSyncInternal()
    }

    companion object {
        private const val DISPOSABLE_LOGIN = "DISPOSABLE_LOGIN"
    }
}