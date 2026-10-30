package com.example.kotlinClean.feature.post.presentation.mvi

/*One-time events that should not be state*/
sealed interface PostEffect {
    data class ShowSnackbar(val message: String) : PostEffect
}