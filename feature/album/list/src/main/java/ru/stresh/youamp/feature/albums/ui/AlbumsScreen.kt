package ru.stresh.youamp.feature.albums.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import org.koin.androidx.compose.koinViewModel
import ru.stresh.youamp.core.ui.OnBottomReached
import ru.stresh.youamp.core.ui.YouAmpPlayerTheme


@Composable
fun AlbumsScreen(onAlbumClick: (id: String) -> Unit) {
    val viewModel: AlbumsViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    AlbumsScreen(
        state = state,
        onRefresh = viewModel::refresh,
        onBottomReached = viewModel::loadMore,
        onAlbumClick = onAlbumClick
    )
}

@Composable
private fun AlbumsScreen(
    state: AlbumsViewModel.StateUi,
    onRefresh: () -> Unit,
    onBottomReached: () -> Unit,
    onAlbumClick: (id: String) -> Unit
) {
    val isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState(
        enabled = { isRefreshing }
    )

    if (pullRefreshState.isRefreshing) {
        onRefresh()
    }

    if (pullRefreshState.isRefreshing && !isRefreshing) {
        pullRefreshState.endRefresh()
    }

    val listState = rememberLazyGridState()
    Scaffold(
        modifier = Modifier.nestedScroll(pullRefreshState.nestedScrollConnection)
    ) { padding ->
        Box(
            modifier = Modifier
                .nestedScroll(pullRefreshState.nestedScrollConnection)
                .padding(padding)
        ) {
            when(state) {
                is AlbumsViewModel.StateUi.Content -> {
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
                        ) { album ->
                            AlbumItem(
                                album = album,
                                onAlbumClick = onAlbumClick
                            )
                        }
                    }
                    PullToRefreshContainer(
                        modifier = Modifier.align(Alignment.TopCenter),
                        state = pullRefreshState,
                    )
                }
                is AlbumsViewModel.StateUi.Progress -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        listState.OnBottomReached {
            onBottomReached()
        }
    }
}

@Composable
private fun AlbumItem(
    album: AlbumUi,
    onAlbumClick: (id: String) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                onAlbumClick(album.id)
            },
    ) {
        SubcomposeAsyncImage(
            model = album.artworkUrl,
            contentDescription = "Album image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            loading = {
                Image(
                    imageVector = Icons.Rounded.Album,
                    contentDescription = "placeholder"
                )
            }
        )
        Text(
            text = album.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 12.dp, end = 12.dp),
            textAlign = TextAlign.Left,
            minLines = 1,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = album.artist,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
            textAlign = TextAlign.Left,
            minLines = 1,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview
private fun AlbumsScreenPreview() {
    YouAmpPlayerTheme {
        val items = listOf(
            AlbumUi(
                id = "1",
                title = "Test",
                artist = "Test artist",
                artworkUrl = null
            ),
            AlbumUi(
                id = "2",
                title = "Test 2",
                artist = "Test artist 2 ",
                artworkUrl = null
            ),
            AlbumUi(
                id = "3",
                title = "Test 3",
                artist = "Test artist 3",
                artworkUrl = null
            )
        )
        val state = AlbumsViewModel.StateUi.Content(isRefreshing = true, items)
        AlbumsScreen(
            state = state,
            onRefresh = {  },
            onBottomReached = {  },
            onAlbumClick = {  }
        )
    }
}