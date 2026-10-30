package com.example.kotlinClean.feature.post.domain.repository

import com.example.kotlinClean.core.common.Resources
import com.example.kotlinClean.feature.post.domain.model.PostModel

interface PostRepository {

    suspend fun getPost(): Resources<List<PostModel>>
}