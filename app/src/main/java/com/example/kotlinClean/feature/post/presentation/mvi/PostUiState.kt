package com.example.kotlinClean.feature.post.presentation.mvi

import com.example.kotlinClean.feature.post.domain.model.PostModel

/*What the screen looks like right now*/
sealed interface PostUiState {
    data object Idle : PostUiState
    data object Loading : PostUiState
    data class Success(val posts: List<PostModel>) : PostUiState
    data class Error(val message: String) : PostUiState
}