package com.booboot.vndbandroid.model.vnstat

data class VNStatItem(
        var similar: List<SimilarNovel> = emptyList(),
        var recommendations: List<SimilarNovel> = emptyList()
)