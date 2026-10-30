package com.example.kotlinClean.feature.post.data

import com.example.kotlinClean.core.common.Resources
import com.example.kotlinClean.feature.post.domain.model.PostModel
import com.example.kotlinClean.feature.post.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/*
interface PostRepositoryImpl {
    suspend fun onGetPost(): List<PostDto>
}*/


@Singleton
class PostRepositoryImpl @Inject constructor(
    private val api: PostApi
) : PostRepository {

    override suspend fun getPost(): Resources<List<PostModel>> =
        withContext(Dispatchers.IO) {
            try {
                val posts = api.getPost().map { it.toDomain() }
                Resources.Success(posts)
            } catch (e: IOException) {
                Resources.Error("Network error: ${e.message}", e)
            } catch (e: HttpException) {
                Resources.Error("HTTP ${e.code()}: ${e.message()}", e)
            } catch (e: Exception) {
                Resources.Error("Unexpected error: ${e.message}", e)
            }
        }
}