package com.booboot.vndbandroid.model.vnstat

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by od on 12/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class VNStatItem(var similar: List<SimilarNovel>? = null,
                      var recommendations: List<SimilarNovel>? = null)
