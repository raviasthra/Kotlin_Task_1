package com.example.kotlinClean.feature.post.presentation.mvi

import com.example.kotlinClean.feature.post.domain.model.PostModel

/*What the user wants to do*/
sealed interface PostIntent {
    data object LoadPosts : PostIntent
    data object Retry : PostIntent
}
