package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.UserLabelDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.UserLabel
import com.booboot.vndbandroid.util.type
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UserLabelsRepository @Inject constructor(var boxStore: BoxStore, var vndbServer: VNDBServer) : Repository<UserLabel>() {
    private fun addItemsToDB(items: List<UserLabel>) {
        boxStore.save(true) { items.map { UserLabelDao(it) } }
    }

    override suspend fun getItems(cachePolicy: CachePolicy<Map<Long, UserLabel>>) = cachePolicy
        .fetchFromMemory { items }
        .fetchFromDatabase { boxStore.get<UserLabelDao, List<UserLabel>> { it.all.map { userLabelDao -> userLabelDao.toBo() } }.associateBy { it.id } }
        .fetchFromNetwork {
            vndbServer
                .get<UserLabel>("ulist-labels", "basic", "(uid = 0)", Options(results = 25, fetchAllPages = true), type())
                .items
                .associateBy { it.id }
        }
        .putInMemory { items = it.toMutableMap() }
        .putInDatabase { addItemsToDB(it.values.toList()) }
        .get { emptyMap() }

    override suspend fun setItems(items: Map<Long, UserLabel>) {
        this@UserLabelsRepository.items = items.toMutableMap()
        addItemsToDB(items.values.toList())
    }
}