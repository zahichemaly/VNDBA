package com.booboot.vndbandroid.api

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

interface VNDBService {
    @Streaming
    @GET("tags.json.gz")
    fun getTags(): Deferred<ResponseBody>

    @Streaming
    @GET("traits.json.gz")
    fun getTraits(): Deferred<ResponseBody>
}