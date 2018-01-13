package com.booboot.vndbandroid.model.vndbandroid

data class AccountItems(
        var vnlist: List<VNlistItem> = emptyList(),
        var votelist: List<VotelistItem> = emptyList(),
        var wishlist: List<WishlistItem> = emptyList()
)