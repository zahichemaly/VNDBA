package com.booboot.vndbandroid.model.vndbandroid

import androidx.recyclerview.widget.DiffUtil

data class VnlistData(
    val items: AccountItems = AccountItems(),
    var diffResult: DiffUtil.DiffResult? = null
)