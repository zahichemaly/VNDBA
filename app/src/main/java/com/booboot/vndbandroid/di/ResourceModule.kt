package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndb.Trait
import com.booboot.vndbandroid.util.type
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class ResourceModule {
    @Provides
    @Singleton
    fun tags(json: ObjectMapper): Map<Int, Tag> {
        val raw = App.context.resources.openRawResource(R.raw.tags)

        return json.readValue<List<Tag>>(raw, type<List<Tag>>()).map { it.id to it }.toMap()
    }

    @Provides
    @Singleton
    fun traits(json: ObjectMapper): Map<Int, Trait> {
        val raw = App.context.resources.openRawResource(R.raw.traits)

        return json.readValue<List<Trait>>(raw, type<List<Trait>>()).map { it.id to it }.toMap()
    }
}