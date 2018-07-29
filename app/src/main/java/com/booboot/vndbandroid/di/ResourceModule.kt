package com.booboot.vndbandroid.di

import android.app.Application
import com.booboot.vndbandroid.api.VNDBService
import com.booboot.vndbandroid.repository.TagsRepository
import com.booboot.vndbandroid.repository.TraitsRepository
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class ResourceModule {
    @Provides
    @Singleton
    fun tags(vndbService: VNDBService, json: ObjectMapper, app: Application) = TagsRepository(vndbService, json, app)

    @Provides
    @Singleton
    fun traits(json: ObjectMapper) = TraitsRepository(json)
}