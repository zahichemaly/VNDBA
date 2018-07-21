package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.util.type
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagsRepository @Inject constructor(var json: ObjectMapper) : Repository<Tag>() {
    override fun getItems(): Single<Map<Int, Tag>> = Single.fromCallable {
        if (items.isNotEmpty()) items
        else {
            val raw = App.context.resources.openRawResource(R.raw.tags)
            json.readValue<List<Tag>>(raw, type<List<Tag>>()).map { it.id to it }.toMap()
        }
    }
}