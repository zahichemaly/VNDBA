package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.model.vndb.Links
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun retrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(Links.VNDB_API)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun vndbService(retrofit: Retrofit): VNDBService = retrofit.create(VNDBService::class.java)
}