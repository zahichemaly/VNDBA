package com.booboot.vndbandroid.di

import dagger.Component
import com.booboot.vndbandroid.ui.restaurantdetail.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [RxModule::class, DataStoreModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}