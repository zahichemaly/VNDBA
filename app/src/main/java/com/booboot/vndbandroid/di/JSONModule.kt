package com.booboot.vndbandroid.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class JSONModule {
    @Provides
    @Singleton
    fun moshi(): Moshi = Moshi.Builder().build()
}