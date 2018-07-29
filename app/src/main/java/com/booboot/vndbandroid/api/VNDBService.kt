package com.booboot.vndbandroid.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming


interface VNDBService {
    @Streaming
    @GET("tags.json.gz")
    fun getTags(): Single<ResponseBody>
}