package com.example.kotlinClean.core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinClean.feature.post.presentation.ui.PostRoute

object Routes {
    const val POSTS = "posts"
}

@Composable
fun AppNavHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.POSTS,
        modifier = modifier
    ) {
        composable(Routes.POSTS) {
            PostRoute(snackbarHostState = snackbarHostState)
        }
    }
}