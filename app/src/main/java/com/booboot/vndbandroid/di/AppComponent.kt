package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.ui.home.MainActivity
import com.booboot.vndbandroid.ui.hometabs.HomeTabsViewModel
import com.booboot.vndbandroid.ui.login.LoginViewModel
import com.booboot.vndbandroid.ui.vndetails.VNDetailsViewModel
import com.booboot.vndbandroid.ui.vnlist.VNListViewModel
import com.booboot.vndbandroid.ui.vntags.TagsViewModel
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
    fun inject(loginViewModel: LoginViewModel)
    fun inject(mainActivity: MainActivity)
    fun inject(homeTabsViewModel: HomeTabsViewModel)
    fun inject(vnListViewModel: VNListViewModel)
    fun inject(vnDetailsViewModel: VNDetailsViewModel)
    fun inject(tagsViewModel: TagsViewModel)
}