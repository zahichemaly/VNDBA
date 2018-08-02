package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.util.type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class VnlistRepository @Inject constructor(var db: DB, var vndbServer: VNDBServer) : ListRepository<Vnlist>() {
    override fun getItemsFromDB(): List<Vnlist> = db.vnlistDao().findAll()

    override fun addItemsToDB(items: List<Vnlist>) = db.vnlistDao().insertAll(items)

    override fun getItemsFromAPI(): Results<Vnlist> = vndbServer
        .get<Vnlist>("vnlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true), type())
        .blockingGet()
}