package com.booboot.vndbandroid.ui.home

import android.app.Application
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.ui.base.StartupSyncViewModel

class HomeViewModel constructor(application: Application) : StartupSyncViewModel(application) {
    init {
        (application as App).appComponent.inject(this)
    }

    fun startupSync() = coroutine(DISPOSABLE_STARTUP_SYNC) {
        startupSyncInternal()
    }

    fun getVns(force: Boolean = true) = coroutine(DISPOSABLE_GET_VNS, !force && accountData.value != null) {
        accountData += accountRepository.getItems()
    }

    companion object {
        private const val DISPOSABLE_STARTUP_SYNC = "DISPOSABLE_STARTUP_SYNC"
        private const val DISPOSABLE_GET_VNS = "DISPOSABLE_GET_VNS"
    }
}