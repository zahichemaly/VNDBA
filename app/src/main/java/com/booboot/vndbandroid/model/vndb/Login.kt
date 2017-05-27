package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Login(var protocol: Int = 0,
                 var client: String = "",
                 var clientver: Double = 0.0,
                 var username: String = "",
                 var password: String = "") : VNDBCommand()
