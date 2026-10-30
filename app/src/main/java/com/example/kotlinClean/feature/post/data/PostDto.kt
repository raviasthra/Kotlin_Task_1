package com.example.kotlinClean.feature.post.data

import com.example.kotlinClean.feature.post.domain.model.PostModel
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

fun PostDto.toDomain(): PostModel = PostModel(
    id = id,
    userId = userId,
    title = title,
    body = body
)