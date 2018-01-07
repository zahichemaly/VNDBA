package com.booboot.vndbandroid.ui.home

import com.booboot.vndbandroid.BuildConfig
import com.booboot.vndbandroid.di.Schedulers
import com.booboot.vndbandroid.ui.Presenter
import io.reactivex.Observable
import javax.inject.Inject

open class MainPresenter @Inject constructor(
        private val schedulers: Schedulers) : Presenter<MainView>() {

    fun loadRestaurant() {
        val observable = Observable.just("")
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .doOnSubscribe { view?.showLoading(true) }
                .doFinally { view?.showLoading(false) }
                .subscribe(this::onNext, this::onError)
        composite.add(observable)
    }

    private fun onNext(result: String) {
        if (result == null) {
            onError(Throwable("Error"))
        } else {
            // Success
        }
    }

    private fun onError(throwable: Throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace()
        view?.showError(throwable.localizedMessage)
    }
}