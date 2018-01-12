package com.booboot.vndbandroid.ui.login

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.di.Schedulers
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndbandroid.VNlistItem
import com.booboot.vndbandroid.ui.Presenter
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

open class LoginPresenter @Inject constructor(
        private val vndbServer: VNDBServer,
        private val schedulers: Schedulers) : Presenter<LoginView>() {

    fun login() {
        val observable = vndbServer.get("vnlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true), 0, object : TypeToken<Results<VNlistItem>>() {})
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .doOnSubscribe { view?.showLoading(true) }
                .doFinally { view?.showLoading(false) }
                .subscribe(this::onNext, this::onError)
        composite.add(observable)
    }

    private fun onNext(result: Results<VNlistItem>) {
        view?.showResult(result)
    }

    private fun onError(throwable: Throwable) {
        view?.showError(throwable.localizedMessage)
    }
}