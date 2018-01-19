package com.booboot.vndbandroid.model.vndbandroid

data class VotelistItem(
        override var vn: Int = 0,
        override var added: Int = 0,
        var vote: Int = 0
) : AccountItem()