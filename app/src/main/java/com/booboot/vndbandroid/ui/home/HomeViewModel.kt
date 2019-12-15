package com.booboot.vndbandroid.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.extensions.plusAssign
import com.booboot.vndbandroid.ui.base.StartupSyncViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HomeViewModel constructor(application: Application) : StartupSyncViewModel(application) {
    val filterData: MutableLiveData<String> = MutableLiveData()

    var filterBarState = BottomSheetBehavior.STATE_HIDDEN

    init {
        (application as App).appComponent.inject(this)
    }

    fun startupSync() = coroutine(DISPOSABLE_STARTUP_SYNC) {
        super.startupSync(this) { accountData.postValue(it) }.await()
    }

    fun getVns(force: Boolean = true) = coroutine(DISPOSABLE_GET_VNS, !force && accountData.value != null) {
        accountData += accountRepository.getItems(this).await()
    }

    fun logout() = coroutine(JOB_LOGOUT) {
        VNDBServer.closeAll().await()
    }

    companion object {
        private const val DISPOSABLE_STARTUP_SYNC = "DISPOSABLE_STARTUP_SYNC"
        private const val DISPOSABLE_GET_VNS = "DISPOSABLE_GET_VNS"
        private const val JOB_LOGOUT = "JOB_LOGOUT"
    }
}