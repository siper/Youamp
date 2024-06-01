package ru.stersh.youamp.feature.playlists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.YouAmpPlayerTheme


@Composable
fun PlaylistsScreen(
    viewModelStoreOwner: ViewModelStoreOwner,
    onPlaylistClick: (id: String) -> Unit
) {
    val viewModel: PlaylistsViewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner)

    val state by viewModel.state.collectAsStateWithLifecycle()

    PlaylistsScreen(
        state = state,
        onRefresh = viewModel::refresh,
        onPlaylistClick = onPlaylistClick
    )
}

@Composable
private fun PlaylistsScreen(
    state: StateUi,
    onRefresh: () -> Unit,
    onPlaylistClick: (id: String) -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    if (pullRefreshState.isRefreshing) {
        onRefresh()
    }

    val isRefreshingState = (state as? StateUi.Content)?.isRefreshing == true
    if (pullRefreshState.isRefreshing && isRefreshingState) {
        pullRefreshState.endRefresh()
    }

    val listState = rememberLazyGridState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullRefreshState.nestedScrollConnection)
    ) {
        when (state) {
            is StateUi.Content -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    items(
                        items = state.items
                    ) { playlist ->
                        PlaylistItem(
                            playlist = playlist,
                            onPlaylistClick = onPlaylistClick
                        )
                    }
                }
                PullToRefreshContainer(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = pullRefreshState,
                )
            }

            is StateUi.Progress -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun PlaylistItem(
    playlist: PlaylistUi,
    onPlaylistClick: (id: String) -> Unit
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        onClick = {
            onPlaylistClick(playlist.id)
        }
    ) {
        Artwork(
            artworkUrl = playlist.artworkUrl,
            placeholder = Icons.Rounded.MusicNote,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Text(
            text = playlist.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Left,
            minLines = 1,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
@Preview
private fun PlaylistsScreenPreview() {
    YouAmpPlayerTheme {
        val items = listOf(
            PlaylistUi(
                id = "1",
                name = "Test",
                artworkUrl = null
            ),
            PlaylistUi(
                id = "2",
                name = "Test 2",
                artworkUrl = null
            ),
            PlaylistUi(
                id = "3",
                name = "Test 3",
                artworkUrl = null
            )
        )
        val state = StateUi.Content(isRefreshing = true, items)
        PlaylistsScreen(
            state = state,
            onRefresh = { },
            onPlaylistClick = { }
        )
    }
}