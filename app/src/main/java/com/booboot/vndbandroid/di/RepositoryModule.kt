package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module that provides application-scoped repository-pattern classes, i.e. classes that can cache
 * and transparently retrieve data from either memory, DB or network when needed.
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

    @Provides
    @Singleton
    fun accountRepository(
            db: DB,
            vnRepository: VNRepository,
            vnlistRepository: VnlistRepository,
            votelistRepository: VotelistRepository,
            wishlistRepository: WishlistRepository,
            schedulers: Schedulers
    ) = AccountRepository(db, vnRepository, vnlistRepository, votelistRepository, wishlistRepository, schedulers)
}