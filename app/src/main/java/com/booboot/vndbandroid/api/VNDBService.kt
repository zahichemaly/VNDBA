package com.booboot.vndbandroid.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

interface VNDBService {
    @Streaming
    @GET("tags.json.gz")
    suspend fun getTags(): ResponseBody

    @Streaming
    @GET("traits.json.gz")
    suspend fun getTraits(): ResponseBody
}