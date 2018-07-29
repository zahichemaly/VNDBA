package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.model.vndb.Links
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun vndbServer(json: ObjectMapper): VNDBServer = VNDBServer(json)

    @Provides
    @Singleton
    fun retrofit(json: ObjectMapper): Retrofit = Retrofit.Builder()
        .baseUrl(Links.VNDB_API)
        .addConverterFactory(JacksonConverterFactory.create(json))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun vndbService(retrofit: Retrofit): VNDBService = retrofit.create(VNDBService::class.java)
}