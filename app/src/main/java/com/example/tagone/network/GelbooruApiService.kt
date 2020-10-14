package com.example.tagone.network

import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://gelbooru.com"

interface GelbooruApiService {
    @GET("/index.php?page=dapi&s=post&q=index&json=1")
    suspend fun getPosts(
        @Query("tags") tags: String,
        @Query("pid") page: Int,
        @Query("limit") limit: Int
    )
}