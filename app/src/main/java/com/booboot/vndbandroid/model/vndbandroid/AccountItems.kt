package com.booboot.vndbandroid.model.vndbandroid

data class AccountItems(
        var vnlist: List<Vnlist> = emptyList(),
        var votelist: List<Votelist> = emptyList(),
        var wishlist: List<Wishlist> = emptyList()
)