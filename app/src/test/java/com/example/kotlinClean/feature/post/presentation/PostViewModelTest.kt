package com.example.kotlinClean.feature.post.presentation

import app.cash.turbine.test
import com.example.kotlinClean.core.MainDispatcherRule
import com.example.kotlinClean.core.common.Resources
import com.example.kotlinClean.feature.post.domain.model.PostModel
import com.example.kotlinClean.feature.post.domain.usecase.PostUseCase
import com.example.kotlinClean.feature.post.presentation.mvi.PostIntent
import com.example.kotlinClean.feature.post.presentation.mvi.PostUiState
import com.example.kotlinClean.feature.post.presentation.viewModel.PostViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var useCase: PostUseCase

    private val post = PostModel(
        id = 1,
        userId = 1,
        title = "Title",
        body = "Body"
    )

    @Before
    fun setUp() {
        useCase = mockk()
    }

    @Test
    fun `Loading then Success`() = runTest {
        // Given: the use case returns a successful list
        coEvery { useCase() } returns Resources.Success(listOf(post))

        // When: the ViewModeml is created (init fires LoadPosts)
        val viewModel = PostViewModel(useCase)

        // Then: state emits Idle → Loading → Success
        viewModel.state.test {
            assertEquals(PostUiState.Idle, awaitItem())
            assertEquals(PostUiState.Loading, awaitItem())
            advanceUntilIdle()
            val success = awaitItem() as PostUiState.Success
            assertEquals(listOf(post), success.posts)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Retry reloads`() = runTest {
        // Given: first call fails, second call succeeds
        coEvery { useCase() } returnsMany listOf(
            Resources.Error("boom"),
            Resources.Success(listOf(post))
        )

        // When: ViewModel loads and fails
        val viewModel = PostViewModel(useCase)
        advanceUntilIdle()
        assertTrue(viewModel.state.value is PostUiState.Error)

        // And: user taps Retry
        viewModel.onIntent(PostIntent.Retry)
        advanceUntilIdle()

        // Then: state is Success and useCase was called twice
        val state = viewModel.state.value as PostUiState.Success
        assertEquals(listOf(post), state.posts)
        coVerify(exactly = 2) { useCase() }
    }
}