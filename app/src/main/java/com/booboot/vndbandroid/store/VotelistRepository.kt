package com.booboot.vndbandroid.store

import com.booboot.vndbandroid.model.vndbandroid.VotelistItem
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.realm.Realm

open class VotelistRepository : ListRepository<VotelistItem>()