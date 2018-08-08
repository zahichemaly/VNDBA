package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.model.vndb.Links
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun vndbServer(moshi: Moshi): VNDBServer = VNDBServer(moshi)

    @Provides
    @Singleton
    fun retrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(Links.VNDB_API)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun vndbService(retrofit: Retrofit): VNDBService = retrofit.create(VNDBService::class.java)
}