package com.booboot.vndbandroid.model.vndbandroid

data class VNlistItem(
        override var vn: Int = 0,
        override var added: Int = 0,
        var status: Int = 0,
        var notes: String? = null
) : AccountItem()