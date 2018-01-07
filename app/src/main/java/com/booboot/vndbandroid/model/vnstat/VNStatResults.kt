package com.booboot.vndbandroid.model.vnstat

data class VNStatResults(
        var success: Boolean = false,
        var result: VNStatItem = VNStatItem(),
        var message: String? = null
)