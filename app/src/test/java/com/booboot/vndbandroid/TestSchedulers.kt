package com.booboot.vndbandroid

import com.booboot.vndbandroid.di.Schedulers

import io.reactivex.Scheduler

class TestSchedulers : Schedulers {
    override fun io(): Scheduler {
        return io.reactivex.schedulers.Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return io.reactivex.schedulers.Schedulers.trampoline()
    }

    override fun newThread(): Scheduler {
        return io.reactivex.schedulers.Schedulers.trampoline()
    }
}