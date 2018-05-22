package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.store.VNRepository
import com.booboot.vndbandroid.store.VnlistRepository
import com.booboot.vndbandroid.store.VotelistRepository
import com.booboot.vndbandroid.store.WishlistRepository
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
internal class RepositoryModule {
    @Provides
    @Singleton
    fun vnlistRepository(db: DB) = VnlistRepository(db)

    @Provides
    @Singleton
    fun votelistRepository(db: DB) = VotelistRepository(db)

    @Provides
    @Singleton
    fun wishlistRepository(db: DB) = WishlistRepository(db)

    @Provides
    @Singleton
    fun vnRepository(db: DB) = VNRepository(db)
}