package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Producer(var id: Int = 0,
                    var developer: Boolean = false,
                    var publisher: Boolean = false,
                    var name: String? = null,
                    var original: String? = null,
                    var type: String? = null) : VNDBCommand()