package com.example.kotlinClean.feature.post.domain.usecase

import com.example.kotlinClean.core.common.Resources
import com.example.kotlinClean.feature.post.domain.model.PostModel
import com.example.kotlinClean.feature.post.domain.repository.PostRepository
import javax.inject.Inject

class PostUseCase @Inject constructor(val postRepository: PostRepository) {

    suspend operator fun invoke(): Resources<List<PostModel>> = postRepository.getPost()

   // suspend fun newCall() : Resources<List<PostModel>> = postRepository.getPost()
}