package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.dao.DB
import com.booboot.vndbandroid.model.vndb.Wishlist
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WishlistRepository @Inject constructor(var db: DB) : ListRepository<Wishlist>() {
    override fun getItemsFromDB(): List<Wishlist> = db.wishlistDao().findAll()

    override fun addItemsToDB(items: List<Wishlist>) = db.wishlistDao().insertAll(items)
}