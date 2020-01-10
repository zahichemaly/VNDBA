package com.booboot.vndbandroid.model.vndbandroid

import com.booboot.vndbandroid.model.vndb.AccountItems

data class VnlistData(
    val tabs: List<HomeTab>,
    val items: Map<Long, AccountItems>
)