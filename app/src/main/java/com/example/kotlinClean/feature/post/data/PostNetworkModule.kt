package com.example.kotlinClean.feature.post.data

import com.example.kotlinClean.feature.post.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PostNetworkModule {

    @Binds
    @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    companion object {
        @Provides
        @Singleton
        fun providePostApi(retrofit: Retrofit): PostApi =
            retrofit.create(PostApi::class.java)
    }
}
