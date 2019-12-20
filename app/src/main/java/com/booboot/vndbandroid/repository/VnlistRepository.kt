package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.VnlistDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class VnlistRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer) : ListRepository<Vnlist>() {
    override fun getItemsFromDB(): List<Vnlist> = boxStore.get<VnlistDao, List<Vnlist>> { it.all.map { vnlistDao -> vnlistDao.toBo() } }

    override fun addItemsToDB(items: List<Vnlist>) = boxStore.save(true) { items.map { VnlistDao(it) } }

    override suspend fun getItemsFromAPI() = vndbServer
        .get<Vnlist>("vnlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true), type())

    suspend fun setItem(vnlist: Vnlist) {
        vndbServer.set("vnlist", vnlist.vn, vnlist, type())
        items[vnlist.vn] = vnlist
        boxStore.save { listOf(VnlistDao(vnlist)) }
    }

    suspend fun deleteItem(vnlist: Vnlist) {
        vndbServer.set<Vnlist>("vnlist", vnlist.vn, null, type())
        items.remove(vnlist.vn)
        boxStore.boxFor<VnlistDao>().remove(vnlist.vn)
    }
}