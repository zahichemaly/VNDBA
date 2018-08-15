package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.WishlistDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WishlistRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer) : ListRepository<Wishlist>() {
    override fun getItemsFromDB(): List<Wishlist> = boxStore.get<WishlistDao, List<Wishlist>> { it.all.map { it.toBo() } }

    override fun addItemsToDB(items: List<Wishlist>) = boxStore.save { items.map { WishlistDao(it) } }

    override fun getItemsFromAPI(): Results<Wishlist> = vndbServer
        .get<Wishlist>("wishlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true, socketIndex = 2), type())
        .blockingGet()
}