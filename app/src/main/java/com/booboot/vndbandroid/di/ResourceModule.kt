package com.booboot.vndbandroid.di

import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndb.Trait
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import dagger.Module
import dagger.Provides
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Singleton

@Module
internal class ResourceModule {
    @Provides
    @Singleton
    fun tags(gson: Gson): Map<Int, Tag> {
        val raw = App.instance.resources.openRawResource(R.raw.tags)
        val reader = JsonReader(BufferedReader(InputStreamReader(raw)))

        return gson.fromJson<List<Tag>>(reader, object : TypeToken<List<Tag>>() {
        }.type).map { it.id to it }.toMap()
    }

    @Provides
    @Singleton
    fun traits(gson: Gson): Map<Int, Trait> {
        val raw = App.instance.resources.openRawResource(R.raw.traits)
        val reader = JsonReader(BufferedReader(InputStreamReader(raw)))

        return gson.fromJson<List<Trait>>(reader, object : TypeToken<List<Trait>>() {
        }.type).map { it.id to it }.toMap()
    }
}