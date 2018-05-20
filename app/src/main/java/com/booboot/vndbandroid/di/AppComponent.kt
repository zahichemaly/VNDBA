package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.ui.home.MainActivity
import com.booboot.vndbandroid.ui.login.LoginViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DatabaseModule::class,
    RxModule::class,
    RepositoryModule::class,
    ResourceModule::class,
    JSONModule::class
])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(loginViewModel: LoginViewModel)
}