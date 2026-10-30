package com.example.kotlinClean.feature.post.data

import retrofit2.http.GET

interface PostApi {

    @GET("/posts")
    suspend fun getPost(): List<PostDto>
}