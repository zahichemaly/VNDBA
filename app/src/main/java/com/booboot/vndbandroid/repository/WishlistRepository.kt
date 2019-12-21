package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.WishlistDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WishlistRepository @Inject constructor(var boxStore: BoxStore, private var vndbServer: VNDBServer) : ListRepository<Wishlist>() {
    override fun getItemsFromDB(): List<Wishlist> = boxStore.get<WishlistDao, List<Wishlist>> { it.all.map { wishlistDao -> wishlistDao.toBo() } }

    override fun addItemsToDB(items: List<Wishlist>) = boxStore.save(true) { items.map { WishlistDao(it) } }

    override suspend fun getItemsFromAPI() = vndbServer
        .get<Wishlist>("wishlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true, socketIndex = 2), type())

    suspend fun setItem(wishlist: Wishlist) {
        vndbServer.set("wishlist", wishlist.vn, wishlist, type())
        items[wishlist.vn] = wishlist
        boxStore.save { listOf(WishlistDao(wishlist)) }
    }

    suspend fun deleteItem(wishlist: Wishlist) {
        vndbServer.set<Vnlist>("wishlist", wishlist.vn, null, type())
        items.remove(wishlist.vn)
        boxStore.boxFor<WishlistDao>().remove(wishlist.vn)
    }
}