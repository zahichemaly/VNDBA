package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Screen(var image: String = "",
                  var rid: Int = 0,
                  var nsfw: Boolean = false,
                  var height: Int = 0,
                  var width: Int = 0) : VNDBCommand()