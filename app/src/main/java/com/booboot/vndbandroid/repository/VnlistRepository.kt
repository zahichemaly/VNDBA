package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.VnlistDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class VnlistRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer) : ListRepository<Vnlist>() {
    override fun getItemsFromDB(): List<Vnlist> = boxStore.get<VnlistDao, List<Vnlist>> { it.all.map { it.toBo() } }

    override fun addItemsToDB(items: List<Vnlist>) = boxStore.save(true) { items.map { VnlistDao(it) } }

    override suspend fun getItemsFromAPI(coroutineScope: CoroutineScope): Results<Vnlist> = vndbServer
        .get<Vnlist>(coroutineScope, "vnlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true), type())
        .await()

    fun setItem(coroutineScope: CoroutineScope, vnlist: Vnlist) = coroutineScope.async(Dispatchers.Default) {
        vndbServer.set(this, "vnlist", vnlist.vn, vnlist, type()).await()
        items[vnlist.vn] = vnlist
        boxStore.save { listOf(VnlistDao(vnlist)) }
        true
    }

    fun deleteItem(coroutineScope: CoroutineScope, vnlist: Vnlist) = coroutineScope.async(Dispatchers.Default) {
        vndbServer.set<Vnlist>(this, "vnlist", vnlist.vn, null, type()).await()
        items.remove(vnlist.vn)
        boxStore.boxFor<VnlistDao>().remove(vnlist.vn)
    }
}