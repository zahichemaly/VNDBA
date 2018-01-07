package com.booboot.vndbandroid.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class JSONModule {
    @Provides
    @Singleton
    fun gson(): Gson {
        return Gson()
    }
}