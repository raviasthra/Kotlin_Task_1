package com.example.kotlinClean.feature.post.domain

import com.example.kotlinClean.core.common.Resources
import com.example.kotlinClean.feature.post.domain.model.PostModel
import com.example.kotlinClean.feature.post.domain.repository.PostRepository
import com.example.kotlinClean.feature.post.domain.usecase.PostUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostUseCaseTest {

    private lateinit var repository: PostRepository
    private lateinit var useCase: PostUseCase

    private val samplePost = PostModel(
        id = 1,
        userId = 1,
        title = "Sample title",
        body = "Sample body"
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = PostUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns success`() = runTest {
        coEvery { repository.getPost() } returns Resources.Success(listOf(samplePost))

        val result = useCase()

        assertTrue(result is Resources.Success)
        assertEquals(listOf(samplePost), (result as Resources.Success).data)
        coVerify(exactly = 1) { repository.getPost() }
    }

    @Test
    fun `invoke returns error when repository fails`() = runTest {
        coEvery { repository.getPost() } returns Resources.Error("Network down")

        val result = useCase()

        assertTrue(result is Resources.Error)
        assertEquals("Network down", (result as Resources.Error).message)
    }

    @Test
    fun `invoke propagates cause from repository error`() = runTest {
        val cause = IllegalStateException("boom")
        coEvery { repository.getPost() } returns Resources.Error("failed", cause)

        val result = useCase() as Resources.Error

        assertEquals(cause, result.cause)
    }
}
