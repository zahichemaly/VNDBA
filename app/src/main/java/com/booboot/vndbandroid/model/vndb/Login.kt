package com.booboot.vndbandroid.model.vndb

data class Login(
        var protocol: Int = 0,
        var client: String = "",
        var clientver: Double = 0.toDouble(),
        var username: String = "",
        var password: String = ""
)