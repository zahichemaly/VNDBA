package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.ui.home.MainActivity
import com.booboot.vndbandroid.ui.login.LoginActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    RxModule::class,
    DataStoreModule::class,
    ResourceModule::class,
    JSONModule::class
])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
}