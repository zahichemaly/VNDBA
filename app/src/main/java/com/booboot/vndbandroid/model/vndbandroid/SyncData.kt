package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndb.Tag
import com.booboot.vndbandroid.model.vndb.Trait

data class SyncData(
    val accountItems: AccountItems,
    val tags: Map<Int, Tag>,
    val traits: Map<Int, Trait>
)