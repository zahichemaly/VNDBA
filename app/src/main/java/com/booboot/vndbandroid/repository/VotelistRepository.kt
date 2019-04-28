package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.VotelistDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Votelist
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VotelistRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer) : ListRepository<Votelist>() {
    override fun getItemsFromDB(): List<Votelist> = boxStore.get<VotelistDao, List<Votelist>> { it.all.map { it.toBo() } }

    override fun addItemsToDB(items: List<Votelist>) = boxStore.save(true) { items.map { VotelistDao(it) } }

    override suspend fun getItemsFromAPI(coroutineScope: CoroutineScope): Results<Votelist> = vndbServer
        .get<Votelist>(coroutineScope, "votelist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true, socketIndex = 1), type())
        .await()

    fun setItem(coroutineScope: CoroutineScope, votelist: Votelist) = coroutineScope.async(Dispatchers.IO) {
        vndbServer.set(this, "votelist", votelist.vn, votelist, type()).await()
        items[votelist.vn] = votelist
        boxStore.save { listOf(VotelistDao(votelist)) }
        true
    }

    fun deleteItem(coroutineScope: CoroutineScope, votelist: Votelist) = coroutineScope.async(Dispatchers.IO) {
        vndbServer.set<Vnlist>(this, "votelist", votelist.vn, null, type()).await()
        items.remove(votelist.vn)
        boxStore.boxFor<VotelistDao>().remove(votelist.vn)
    }
}