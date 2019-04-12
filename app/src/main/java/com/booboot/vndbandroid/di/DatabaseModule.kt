package com.booboot.vndbandroid.di

import android.app.Application
import com.booboot.vndbandroid.dao.MyObjectBox
import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
internal class DatabaseModule {
    @Provides
    @Singleton
    fun boxStore(application: Application): BoxStore = MyObjectBox.builder().androidContext(application).build()
}