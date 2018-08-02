package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.ui.home.HomeActivity
import com.booboot.vndbandroid.ui.home.HomeViewModel
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
    ApiModule::class,
    RepositoryModule::class,
    JSONModule::class
])
interface AppComponent {
    fun inject(loginViewModel: LoginViewModel)
    fun inject(homeActivity: HomeActivity)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(homeTabsViewModel: HomeTabsViewModel)
    fun inject(vnListViewModel: VNListViewModel)
    fun inject(vnDetailsViewModel: VNDetailsViewModel)
    fun inject(tagsViewModel: TagsViewModel)
}