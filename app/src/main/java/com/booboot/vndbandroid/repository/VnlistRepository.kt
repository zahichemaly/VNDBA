package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.model.vndb.Vnlist
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class VnlistRepository @Inject constructor(var db: DB) : ListRepository<Vnlist>() {
    override fun getItemsFromDB(): List<Vnlist> = db.vnlistDao().findAll()

    override fun addItemsToDB(items: List<Vnlist>) = db.vnlistDao().insertAll(items)
}