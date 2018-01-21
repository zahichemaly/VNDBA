package com.booboot.vndbandroid.di

import io.reactivex.Scheduler

interface Schedulers {
    fun io(): Scheduler
    fun ui(): Scheduler
    fun newThread(): Scheduler
    fun current(): Scheduler
    fun computation(): Scheduler
}