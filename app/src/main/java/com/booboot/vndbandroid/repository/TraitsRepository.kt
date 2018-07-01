package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.App
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.model.vndb.Trait
import com.booboot.vndbandroid.util.type
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraitsRepository @Inject constructor(var json: ObjectMapper) : Repository<Trait>() {
    override fun getMapItems(): Single<Map<Int, Trait>> = Single.fromCallable {
        if (items.isNotEmpty()) items
        else {
            val raw = App.context.resources.openRawResource(R.raw.traits)
            json.readValue<List<Trait>>(raw, type<List<Trait>>()).map { it.id to it }.toMap()
        }
    }
}