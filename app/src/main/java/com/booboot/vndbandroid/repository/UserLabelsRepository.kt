package com.booboot.vndbandroid.repository

import com.booboot.vndbandroid.api.VNDBServer
import com.booboot.vndbandroid.dao.UserLabelDao
import com.booboot.vndbandroid.extensions.get
import com.booboot.vndbandroid.extensions.save
import com.booboot.vndbandroid.model.vndb.Label.Companion.ALL_VOTES
import com.booboot.vndbandroid.model.vndb.Label.Companion.CLEAR_FILTERS
import com.booboot.vndbandroid.model.vndb.Label.Companion.NO_LABELS
import com.booboot.vndbandroid.model.vndb.Label.Companion.STATUSES
import com.booboot.vndbandroid.model.vndb.Label.Companion.VOTELISTS
import com.booboot.vndbandroid.model.vndb.Label.Companion.VOTE_CONTROLS
import com.booboot.vndbandroid.model.vndb.Label.Companion.WISHLISTS
import com.booboot.vndbandroid.model.vndb.Options
import com.booboot.vndbandroid.model.vndb.UserLabel
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.util.type
import com.chibatching.kotpref.bulk
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
                .toSortedMap()
        }
        .putInMemory { items = it.toMutableMap() }
        .putInDatabase { addItemsToDB(it.values.toList()) }
        .get { emptyMap() }

    fun selectAsFilter(labelId: Long): Set<Long> {
        when {
            labelId == CLEAR_FILTERS -> Preferences.selectedFilters.clear()
            labelId != NO_LABELS -> Preferences.bulk {
                when (val labelIdString = labelId.toString()) {
                    in selectedFilters -> selectedFilters.remove(labelIdString)
                    else -> {
                        when (labelId) {
                            in STATUSES -> selectedFilters.removeAll(STATUSES.map { it.toString() })
                            in WISHLISTS -> selectedFilters.removeAll(WISHLISTS.map { it.toString() })
                            in VOTELISTS -> selectedFilters.removeAll(VOTE_CONTROLS.map { it.toString() })
                            in VOTE_CONTROLS -> selectedFilters.removeAll(ALL_VOTES.map { it.toString() })
                        }
                        selectedFilters.add(labelIdString)
                    }
                }
            }
        }
        return getSelectedFilters()
    }

    private fun getSelectedFilters() = Preferences.selectedFilters.mapNotNull { it.toLongOrNull() }.toHashSet()

    override suspend fun setItems(items: Map<Long, UserLabel>) {
        this@UserLabelsRepository.items = items.toMutableMap()
        addItemsToDB(items.values.toList())
    }
}