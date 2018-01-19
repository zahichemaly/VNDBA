package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.model.vndbandroid.VNlistItem
import com.booboot.vndbandroid.model.vndbandroid.VotelistItem
import com.booboot.vndbandroid.model.vndbandroid.WishlistItem
import com.booboot.vndbandroid.store.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module that provides application-scoped data stores, i.e. stores that can cache data as long as the app lives.
 * Especially useful to replace static fields, or to retain data when the configuration changes: in a Presenter,
 *
 * @Inject a store, set its data when it is fetched, and get it after configuration changes to retrieve it.
 */
@Module
internal class DataStoreModule {
    @Provides
    @Singleton
    fun vnlistRepository(): ListRepository<VNlistItem> {
        return VnlistRepository()
    }

    @Provides
    @Singleton
    fun votelistRepository(): ListRepository<VotelistItem> {
        return VotelistRepository()
    }

    @Provides
    @Singleton
    fun wishlistRepository(): ListRepository<WishlistItem> {
        return WishlistRepository()
    }
}