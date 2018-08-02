package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.util.type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WishlistRepository @Inject constructor(var db: DB, var vndbServer: VNDBServer) : ListRepository<Wishlist>() {
    override fun getItemsFromDB(): List<Wishlist> = db.wishlistDao().findAll()

    override fun addItemsToDB(items: List<Wishlist>) = db.wishlistDao().insertAll(items)

    override fun getItemsFromAPI(): Results<Wishlist> = vndbServer
        .get<Wishlist>("wishlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true, socketIndex = 2), type())
        .blockingGet()
}