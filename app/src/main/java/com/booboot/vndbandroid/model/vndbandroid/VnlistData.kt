package com.booboot.vndbandroid.model.vndbandroid

data class VnlistData(
    val tabs: List<HomeTab>,
    val items: Map<Long, AccountItems>
)