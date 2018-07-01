package com.booboot.vndbandroid.di

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
    fun tags(json: ObjectMapper) = TagsRepository(json)

    @Provides
    @Singleton
    fun traits(json: ObjectMapper) = TraitsRepository(json)
}