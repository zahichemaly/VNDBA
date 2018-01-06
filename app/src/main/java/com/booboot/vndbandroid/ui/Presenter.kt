package com.booboot.vndbandroid.ui

import io.reactivex.disposables.CompositeDisposable

abstract class Presenter<T> {
    protected var view: T? = null
    protected var composite = CompositeDisposable()

    fun attachView(view: T) {
        this.view = view
    }

    fun detachView() {
        composite.clear()
        this.view = null
    }
}