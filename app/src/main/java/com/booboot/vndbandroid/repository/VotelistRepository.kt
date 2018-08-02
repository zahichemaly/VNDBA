package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.util.type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VotelistRepository @Inject constructor(var db: DB, var vndbServer: VNDBServer) : ListRepository<Votelist>() {
    override fun getItemsFromDB(): List<Votelist> = db.votelistDao().findAll()

    override fun addItemsToDB(items: List<Votelist>) = db.votelistDao().insertAll(items)

    override fun getItemsFromAPI(): Results<Votelist> = vndbServer
        .get<Votelist>("votelist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true, socketIndex = 1), type())
        .blockingGet()
}