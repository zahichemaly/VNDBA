package com.booboot.vndbandroid.di

import android.app.Application
import com.booboot.vndbandroid.dao.MyObjectBox
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoxManager @Inject constructor(val application: Application) {
    var boxStore = init()

    @Synchronized
    fun reset() {
        boxStore.close()
        boxStore.deleteAllFiles()
        boxStore = init()
    }

    fun init(): BoxStore = MyObjectBox.builder().androidContext(application).build()
}