package com.booboot.vndbandroid.ui.vndetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.VN
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VNDetailsViewModel constructor(application: Application) : BaseViewModel(application) {
    @Inject lateinit var vnRepository: VNRepository
    @Inject lateinit var accountRepository: AccountRepository
    val vnData: MutableLiveData<VN> = MutableLiveData()
    val accountData: MutableLiveData<AccountItems> = MutableLiveData()

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadVn(vnId: Long, force: Boolean = true) {
        if (!force && vnData.value != null) return
        if (disposables.contains(DISPOSABLE_VN)) return

        disposables[DISPOSABLE_VN] = vnRepository.getItem(vnId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingData.value = true }
            .doFinally {
                loadingData.value = false
                disposables.remove(DISPOSABLE_VN)
            }
            .subscribe({
                vnData.value = it
            }, ::onError)
    }

    fun loadAccount(force: Boolean = true) {
        if (!force && accountData.value != null) return
        if (disposables.contains(DISPOSABLE_ACCOUNT)) return

        disposables[DISPOSABLE_ACCOUNT] = accountRepository.getItems()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { disposables.remove(DISPOSABLE_ACCOUNT) }
            .subscribe({ accountData.value = it }, ::onError)
    }

    companion object {
        private const val DISPOSABLE_VN = "DISPOSABLE_VN"
        private const val DISPOSABLE_ACCOUNT = "DISPOSABLE_ACCOUNT"
    }
}