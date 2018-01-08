package com.booboot.vndbandroid.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Singleton

@Module
class RxModule {
    @Provides
    @Singleton
    fun schedulers(): Schedulers = object : Schedulers {
        override fun io(): Scheduler = io.reactivex.schedulers.Schedulers.io()
        override fun ui(): Scheduler = AndroidSchedulers.mainThread()
        override fun newThread(): Scheduler = io.reactivex.schedulers.Schedulers.newThread()
        override fun current(): Scheduler = io.reactivex.schedulers.Schedulers.trampoline()
    }
}