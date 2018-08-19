package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.VotelistDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VotelistRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer) : ListRepository<Votelist>() {
    override fun getItemsFromDB(): List<Votelist> = boxStore.get<VotelistDao, List<Votelist>> { it.all.map { it.toBo() } }

    override fun addItemsToDB(items: List<Votelist>) = boxStore.save(true) { items.map { VotelistDao(it) } }

    override fun getItemsFromAPI(): Results<Votelist> = vndbServer
        .get<Votelist>("votelist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true, socketIndex = 1), type())
        .blockingGet()
}