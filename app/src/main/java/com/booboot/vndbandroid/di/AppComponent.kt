package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.ui.login.LoginActivity
import com.booboot.vndbandroid.ui.home.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RxModule::class, DataStoreModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity: LoginActivity)
}