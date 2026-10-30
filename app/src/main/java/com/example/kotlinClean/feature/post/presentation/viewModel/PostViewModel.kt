package com.example.kotlinClean.feature.post.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinClean.core.common.Resources
import com.example.kotlinClean.feature.post.domain.repository.PostRepository
import com.example.kotlinClean.feature.post.domain.usecase.PostUseCase
import com.example.kotlinClean.feature.post.presentation.mvi.PostEffect
import com.example.kotlinClean.feature.post.presentation.mvi.PostIntent
import com.example.kotlinClean.feature.post.presentation.mvi.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCase: PostUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PostUiState>(PostUiState.Idle)
    val state: StateFlow<PostUiState> = _state.asStateFlow()

    private val _effect = Channel<PostEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()                       // public, Flow

    init {
        onIntent(PostIntent.LoadPosts)
    }

    fun onIntent(intent: PostIntent) {
        when (intent) {
            PostIntent.LoadPosts -> loadPosts()
            PostIntent.Retry -> loadPosts()
        }
    }

    private fun loadPosts() {
        if (_state.value is PostUiState.Loading) return

        viewModelScope.launch {
            _state.value = PostUiState.Loading

            when (val result = postUseCase()) {
                is Resources.Success -> {
                    _effect.send(PostEffect.ShowSnackbar("Post loaded successfully"))
                    _state.value = PostUiState.Success(result.data)
                }

                is Resources.Error -> {
                    _state.value = PostUiState.Error(result.message)
                    _effect.send(PostEffect.ShowSnackbar("Loaded"))
                }

                Resources.Loading -> Unit
            }
        }
    }
}
