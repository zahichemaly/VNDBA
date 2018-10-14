package com.booboot.vndbandroid.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.extensions.minus
import com.booboot.vndbandroid.extensions.plus
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.ui.base.StartupSyncViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class HomeViewModel constructor(application: Application) : StartupSyncViewModel(application) {
    val accountData: MutableLiveData<AccountItems> = MutableLiveData()
    val filterData: MutableLiveData<String> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun startupSync() {
        if (disposables.contains(DISPOSABLE_STARTUP_SYNC)) return

        disposables[DISPOSABLE_STARTUP_SYNC] = startupSyncSingle { accountData.value = it }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.plus() }
            .doFinally {
                loadingData.minus()
                disposables.remove(DISPOSABLE_STARTUP_SYNC)
            }
            .subscribe({}, ::onError)
    }

    fun getVns(force: Boolean = true, toSyncData: Boolean = false) {
        if (!force && accountData.value != null) return
        if (disposables.contains(DISPOSABLE_GET_VNS)) return

        disposables[DISPOSABLE_GET_VNS] = accountRepository.getItems()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { disposables.remove(DISPOSABLE_GET_VNS) }
            .subscribe({
                if (toSyncData) syncAccountData.value = it
                else accountData.value = it
            }, ::onError)
    }

    companion object {
        private const val DISPOSABLE_STARTUP_SYNC = "DISPOSABLE_STARTUP_SYNC"
        private const val DISPOSABLE_GET_VNS = "DISPOSABLE_GET_VNS"
    }
}