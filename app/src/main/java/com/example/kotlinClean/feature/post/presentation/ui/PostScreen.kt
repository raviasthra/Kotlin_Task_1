package com.example.kotlinClean.feature.post.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kotlinClean.feature.post.domain.model.PostModel
import com.example.kotlinClean.feature.post.presentation.mvi.PostEffect
import com.example.kotlinClean.feature.post.presentation.mvi.PostIntent
import com.example.kotlinClean.feature.post.presentation.mvi.PostUiState
import com.example.kotlinClean.feature.post.presentation.viewModel.PostViewModel
import com.example.kotlinClean.ui.theme.KotlinTheme

@Composable
fun PostRoute(
    padding: PaddingValues,
    viewModel: PostViewModel = hiltViewModel(),
    scnackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PostEffect.ShowSnackbar ->
                    scnackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    PostScreen(state = state, onIntent = viewModel::onIntent, padding)
}

@Composable
fun PostScreen(
    state: PostUiState,
    onIntent: (PostIntent) -> Unit,
    padding: PaddingValues
) {
    when (state) {
        PostUiState.Idle -> Unit

        PostUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is PostUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    Modifier.fillMaxSize().padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: ${state.message}")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { onIntent(PostIntent.Retry) }) {
                        Text("Retry")
                    }
                }
            }
        }

        is PostUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.posts, key = { it.id }) { post ->
                    PostItem(post)
                }
            }
        }
    }
}

@Composable
private fun PostItem(post: PostModel) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, name = "PostItem")
@Composable
private fun PostItemPreview() {
    KotlinTheme {
        PostItem(
            PostModel(
                id = 1,
                userId = 5,
                title = "Preview Title",
                body = "Preview body text that is long enough to. Preview body text that is long enough to" +
                        "Preview body text that is long enough to "
            )
        )
    }
}