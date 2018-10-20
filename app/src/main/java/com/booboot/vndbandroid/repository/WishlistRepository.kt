package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.WishlistDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.model.vndbandroid.EVENT_VNLIST_CHANGED
import com.booboot.vndbandroid.service.EventReceiver
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WishlistRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer) : ListRepository<Wishlist>() {
    override fun getItemsFromDB(): List<Wishlist> = boxStore.get<WishlistDao, List<Wishlist>> { it.all.map { it.toBo() } }

    override fun addItemsToDB(items: List<Wishlist>) = boxStore.save(true) { items.map { WishlistDao(it) } }

    override fun getItemsFromAPI(): Results<Wishlist> = vndbServer
        .get<Wishlist>("wishlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true, socketIndex = 2), type())
        .blockingGet()

    fun setItem(wishlist: Wishlist): Completable = Completable.fromAction {
        vndbServer.set("wishlist", wishlist.vn, wishlist, type()).blockingAwait()
        items[wishlist.vn] = wishlist
        boxStore.save { listOf(WishlistDao(wishlist)) }
        EventReceiver.send(EVENT_VNLIST_CHANGED)
    }

    fun deleteItem(wishlist: Wishlist): Completable = Completable.fromAction {
        vndbServer.set<Vnlist>("wishlist", wishlist.vn, null, type()).blockingAwait()
        items.remove(wishlist.vn)
        boxStore.boxFor<WishlistDao>().remove(wishlist.vn)
        EventReceiver.send(EVENT_VNLIST_CHANGED)
    }
}