package com.booboot.vndbandroid.model.vnstat

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class VNStatResults(var isSuccess: Boolean = false,
                         var nextUpdate: Long = 0,
                         var result: VNStatItem? = null,
                         var message: String? = null)