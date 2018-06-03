package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.api.VNDBServer
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RxModule {
    @Provides
    @Singleton
    fun vndbServer(json: ObjectMapper): VNDBServer = VNDBServer(json)
}