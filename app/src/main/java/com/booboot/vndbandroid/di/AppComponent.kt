package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.ui.home.HomeActivity
import com.booboot.vndbandroid.ui.home.HomeViewModel
import com.booboot.vndbandroid.ui.vnlist.VNListViewModel
import com.booboot.vndbandroid.ui.login.LoginViewModel
import com.booboot.vndbandroid.ui.search.SearchViewModel
import com.booboot.vndbandroid.ui.slideshow.SlideshowViewModel
import com.booboot.vndbandroid.ui.vndetails.VNDetailsViewModel
import com.booboot.vndbandroid.ui.vnrelations.RelationsViewModel
import com.booboot.vndbandroid.ui.vnsummary.SummaryViewModel
import com.booboot.vndbandroid.ui.vntags.TagsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DatabaseModule::class,
    ApiModule::class,
    JSONModule::class
])
interface AppComponent {
    fun inject(loginViewModel: LoginViewModel)
    fun inject(homeActivity: HomeActivity)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(vnListViewModel: VNListViewModel)
    fun inject(vnDetailsViewModel: VNDetailsViewModel)
    fun inject(tagsViewModel: TagsViewModel)
    fun inject(summaryViewModel: SummaryViewModel)
    fun inject(relationsViewModel: RelationsViewModel)
    fun inject(slideshowViewModel: SlideshowViewModel)
    fun inject(searchViewModel: SearchViewModel)
}