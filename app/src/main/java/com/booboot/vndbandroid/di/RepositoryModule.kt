package com.booboot.vndbandroid.di

import android.app.Application
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.TagsRepository
import com.booboot.vndbandroid.repository.TraitsRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.repository.VnlistRepository
import com.booboot.vndbandroid.repository.VotelistRepository
import com.booboot.vndbandroid.repository.WishlistRepository
import com.fasterxml.jackson.databind.ObjectMapper
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
    fun vnlistRepository(db: DB, vndbServer: VNDBServer) = VnlistRepository(db, vndbServer)

    @Provides
    @Singleton
    fun votelistRepository(db: DB, vndbServer: VNDBServer) = VotelistRepository(db, vndbServer)

    @Provides
    @Singleton
    fun wishlistRepository(db: DB, vndbServer: VNDBServer) = WishlistRepository(db, vndbServer)

    @Provides
    @Singleton
    fun vnRepository(db: DB, vndbServer: VNDBServer) = VNRepository(db, vndbServer)

    @Provides
    @Singleton
    fun accountRepository(
        db: DB,
        vnRepository: VNRepository,
        vnlistRepository: VnlistRepository,
        votelistRepository: VotelistRepository,
        wishlistRepository: WishlistRepository
    ) = AccountRepository(db, vnRepository, vnlistRepository, votelistRepository, wishlistRepository)

    @Provides
    @Singleton
    fun tagsRepository(vndbService: VNDBService, json: ObjectMapper, app: Application) = TagsRepository(vndbService, json, app)

    @Provides
    @Singleton
    fun traitsRepository(json: ObjectMapper) = TraitsRepository(json)
}