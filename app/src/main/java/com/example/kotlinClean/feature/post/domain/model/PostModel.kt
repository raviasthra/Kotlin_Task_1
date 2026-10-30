package com.example.kotlinClean.feature.post.domain.model

data class PostModel(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)