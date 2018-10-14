package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.VnlistDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndbandroid.EVENT_VNLIST_CHANGED
import com.booboot.vndbandroid.service.EventReceiver
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class VnlistRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer) : ListRepository<Vnlist>() {
    override fun getItemsFromDB(): List<Vnlist> = boxStore.get<VnlistDao, List<Vnlist>> { it.all.map { it.toBo() } }

    override fun addItemsToDB(items: List<Vnlist>) = boxStore.save(true) { items.map { VnlistDao(it) } }

    override fun getItemsFromAPI(): Results<Vnlist> = vndbServer
        .get<Vnlist>("vnlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true), type())
        .blockingGet()

    fun setItem(vnlist: Vnlist) = Completable.fromAction {
        vndbServer.set("vnlist", vnlist.vn, vnlist, type()).blockingAwait()
        items[vnlist.vn] = vnlist
        boxStore.save { listOf(VnlistDao(vnlist)) }
        EventReceiver.send(EVENT_VNLIST_CHANGED)
    }

    fun deleteItem(vnlist: Vnlist) = Completable.fromAction {
        vndbServer.set<Vnlist>("vnlist", vnlist.vn, null, type()).blockingAwait()
        items.remove(vnlist.vn)
        boxStore.boxFor<VnlistDao>().remove(vnlist.vn)
        EventReceiver.send(EVENT_VNLIST_CHANGED)
    }
}