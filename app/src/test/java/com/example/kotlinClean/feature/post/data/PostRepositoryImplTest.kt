package com.example.kotlinClean.feature.post.data

import com.example.kotlinClean.core.common.Resources
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import org.junit.Assert.assertTrue

class PostRepositoryImplTest {

    private lateinit var api: PostApi
    private lateinit var repository: PostRepositoryImpl

    private val dto = PostDto(userId = 1, id = 1, title = "T", body = "B")

    @Before
    fun setUp() {
        api = mockk()
        repository = PostRepositoryImpl(api)
    }

    @Test
    fun `getPosts maps DTO to domain on success`() = runTest {   // ← runTest wrapper
        coEvery { api.getPost() } returns listOf(dto)          // ← coEvery, not every

        val result = repository.getPost() as Resources.Success

        assertEquals(1, result.data.size)
        assertEquals(dto.id, result.data[0].id)
        assertEquals(dto.title, result.data[0].title)
        coVerify(exactly = 1) { api.getPost() }                // ← coVerify
    }

    @Test
    fun `getPosts returns Error on IOException`() = runTest {
        coEvery { api.getPost() } throws IOException("no network")

        val result = repository.getPost()

        assertTrue(result is Resources.Error)
        assertTrue((result as Resources.Error).message.contains("Network error"))
    }
}