package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.WishlistDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.Results
import com.booboot.vndbandroid.model.vndb.Vnlist
import com.booboot.vndbandroid.model.vndb.Wishlist
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WishlistRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer) : ListRepository<Wishlist>() {
    override fun getItemsFromDB(): List<Wishlist> = boxStore.get<WishlistDao, List<Wishlist>> { it.all.map { it.toBo() } }

    override fun addItemsToDB(items: List<Wishlist>) = boxStore.save(true) { items.map { WishlistDao(it) } }

    override suspend fun getItemsFromAPI(coroutineScope: CoroutineScope): Results<Wishlist> = vndbServer
        .get<Wishlist>(coroutineScope, "wishlist", "basic", "(uid = 0)", Options(results = 100, fetchAllPages = true, socketIndex = 2), type())
        .await()

    fun setItem(coroutineScope: CoroutineScope, wishlist: Wishlist) = coroutineScope.async(Dispatchers.Default) {
        vndbServer.set(this, "wishlist", wishlist.vn, wishlist, type()).await()
        items[wishlist.vn] = wishlist
        boxStore.save { listOf(WishlistDao(wishlist)) }
        true
    }

    fun deleteItem(coroutineScope: CoroutineScope, wishlist: Wishlist) = coroutineScope.async(Dispatchers.Default) {
        vndbServer.set<Vnlist>(this, "wishlist", wishlist.vn, null, type()).await()
        items.remove(wishlist.vn)
        boxStore.boxFor<WishlistDao>().remove(wishlist.vn)
    }
}