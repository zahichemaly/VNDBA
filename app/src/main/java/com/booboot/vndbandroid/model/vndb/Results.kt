package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Results(var num: Int = 0,
                   var more: Boolean = false,
                   var items: List<Item>? = null) : VNDBCommand()