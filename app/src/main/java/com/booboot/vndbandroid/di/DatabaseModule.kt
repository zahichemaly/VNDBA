package com.booboot.vndbandroid.di

import android.app.Application
import androidx.room.Room
import com.booboot.vndbandroid.dao.DB
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
internal class DatabaseModule {
    @Provides
    @Singleton
    fun db(application: Application): DB =
            Room.databaseBuilder(application,
                    DB::class.java, "VNDB_ANDROID")
                    .build()
}