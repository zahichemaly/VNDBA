package com.booboot.vndbandroid.model.vndb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Fields(var vote: Int = 0,
             var status: Int = 0,
             var notes: String? = null,
             var priority: Int = 0) : VNDBCommand()