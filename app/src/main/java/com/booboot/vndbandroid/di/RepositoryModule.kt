package com.booboot.vndbandroid.di

import android.app.Application
import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.repository.AccountRepository
import com.booboot.vndbandroid.repository.TagsRepository
import com.booboot.vndbandroid.repository.TraitsRepository
import com.booboot.vndbandroid.repository.VNRepository
import com.booboot.vndbandroid.repository.VnlistRepository
import com.booboot.vndbandroid.repository.VotelistRepository
import com.booboot.vndbandroid.repository.WishlistRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import javax.inject.Singleton

/**
 * Module that provides application-scoped repository-pattern classes, i.e. classes that can cache
 * and transparently retrieve data from either memory, DB or network when needed.
 */
@Module
internal class RepositoryModule {
    @Provides
    @Singleton
    fun vnlistRepository(boxStore: BoxStore, vndbServer: VNDBServer) = VnlistRepository(boxStore, vndbServer)

    @Provides
    @Singleton
    fun votelistRepository(boxStore: BoxStore, vndbServer: VNDBServer) = VotelistRepository(boxStore, vndbServer)

    @Provides
    @Singleton
    fun wishlistRepository(boxStore: BoxStore, vndbServer: VNDBServer) = WishlistRepository(boxStore, vndbServer)

    @Provides
    @Singleton
    fun vnRepository(boxStore: BoxStore, vndbServer: VNDBServer, moshi: Moshi) = VNRepository(boxStore, vndbServer, moshi)

    @Provides
    @Singleton
    fun accountRepository(
        vnRepository: VNRepository,
        vnlistRepository: VnlistRepository,
        votelistRepository: VotelistRepository,
        wishlistRepository: WishlistRepository
    ) = AccountRepository(vnRepository, vnlistRepository, votelistRepository, wishlistRepository)

    @Provides
    @Singleton
    fun tagsRepository(vndbService: VNDBService, moshi: Moshi, app: Application, boxStore: BoxStore) = TagsRepository(vndbService, moshi, app, boxStore)

    @Provides
    @Singleton
    fun traitsRepository(vndbService: VNDBService, moshi: Moshi, app: Application, boxStore: BoxStore) = TraitsRepository(vndbService, moshi, app, boxStore)
}