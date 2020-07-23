package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.UserListDao
import com.booboot.vndbandroid.di.BoxManager
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.UserList
import com.booboot.vndbandroid.util.type
import io.objectbox.kotlin.boxFor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UserListRepository @Inject constructor(var boxManager: BoxManager, private var vndbServer: VNDBServer) : Repository<UserList>() {
    private fun addItemsToDB(items: List<UserList>) {
        boxManager.boxStore.save(true) { items.map { UserListDao(it, boxManager.boxStore) } }
    }

    override suspend fun getItems(cachePolicy: CachePolicy<Map<Long, UserList>>) = cachePolicy
        .fetchFromMemory { items }
        .fetchFromDatabase { boxManager.boxStore.get<UserListDao, List<UserList>> { it.all.map { userListDao -> userListDao.toBo() } }.associateBy { it.vn } }
        .fetchFromNetwork {
            vndbServer
                .get<UserList>("ulist", "basic,labels", "(uid = 0)", Options(results = 100, fetchAllPages = true), type())
                .items
                .associateBy { it.vn }
        }
        .putInMemory { if (cachePolicy.enabled) items = it.toMutableMap() }
        .putInDatabase { if (cachePolicy.enabled) addItemsToDB(it.values.toList()) }
        .get { emptyMap() }

    override suspend fun setItems(items: Map<Long, UserList>) {
        this@UserListRepository.items = items.toMutableMap()
        addItemsToDB(items.values.toList())
    }

    suspend fun setItem(userList: UserList, changesMap: Map<String, @JvmSuppressWildcards Any?>) {
        vndbServer.set("ulist", userList.vn, changesMap, type())
        items[userList.vn] = userList
        boxManager.boxStore.save { listOf(UserListDao(userList, boxManager.boxStore)) }
    }

    suspend fun deleteItem(userList: UserList) {
        vndbServer.set<UserList>("ulist", userList.vn, null, type())
        items.remove(userList.vn)
        boxManager.boxStore.boxFor<UserListDao>().remove(userList.vn)
    }
}