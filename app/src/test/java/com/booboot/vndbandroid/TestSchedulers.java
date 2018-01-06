package com.booboot.vndbandroid;

import com.booboot.vndbandroid.di.Schedulers;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Scheduler;

public class TestSchedulers implements Schedulers {
    @Override
    public Scheduler io() {
        return io.reactivex.schedulers.Schedulers.trampoline();
    }

    @Override
    public Scheduler ui() {
        return io.reactivex.schedulers.Schedulers.trampoline();
    }

    @NotNull
    @Override
    public Scheduler newThread() {
        return io.reactivex.schedulers.Schedulers.trampoline();
    }
}