package com.booboot.vndbandroid.model.vndbandroid

data class WishlistItem(
        override var vn: Int = 0,
        override var added: Int = 0,
        var priority: Int = 0
) : AccountItem()