package com.example.kotlinClean.core.common

import com.example.kotlinClean.feature.post.domain.model.PostModel

sealed class Resources<out T> {
    data class Success<T>(val data: T) : Resources<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Resources<Nothing>()
    data object Loading : Resources<Nothing>()
}

