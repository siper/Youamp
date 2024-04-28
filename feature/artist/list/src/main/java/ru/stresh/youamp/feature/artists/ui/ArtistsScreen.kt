package ru.stresh.youamp.feature.artists.ui

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun ArtistsScreen(onArtistClick: (id: String) -> Unit) {
    val viewModel: ArtistsViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    ArtistsScreen(
        state = state,
        onRefresh = viewModel::refresh,
        onBottomReached = viewModel::loadMore,
        onArtistClick = onArtistClick
    )
}

@Composable
private fun ArtistsScreen(
    state: ArtistsViewModel.StateUi,
    onRefresh: () -> Unit,
    onBottomReached: () -> Unit,
    onArtistClick: (id: String) -> Unit
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
                is ArtistsViewModel.StateUi.Content -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
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
                                artist = album,
                                onAlbumClick = onArtistClick
                            )
                        }
                    }
                    PullToRefreshContainer(
                        modifier = Modifier.align(Alignment.TopCenter),
                        state = pullRefreshState,
                    )
                }
                is ArtistsViewModel.StateUi.Progress -> {
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
    artist: ArtistUi,
    onAlbumClick: (id: String) -> Unit
) {
    val selectableShape = remember { RoundedCornerShape(36.dp) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(selectableShape)
            .clickable {
                onAlbumClick(artist.id)
            },
    ) {
        val circleShape = remember { CircleShape }
        SubcomposeAsyncImage(
            model = artist.artworkUrl,
            contentDescription = "Album image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(circleShape)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = circleShape
                ),
            loading = {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "placeholder",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        )
        Text(
            text = artist.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 12.dp, end = 12.dp),
            textAlign = TextAlign.Center,
            minLines = 2,
            maxLines = 2,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
@Preview
private fun AlbumsScreenPreview() {
    YouAmpPlayerTheme {
        val items = listOf(
            ArtistUi(
                id = "1",
                name = "Test",
                albumCount = 2,
                artworkUrl = null
            ),
            ArtistUi(
                id = "2",
                name = "Test 2",
                albumCount = 10,
                artworkUrl = null
            ),
            ArtistUi(
                id = "3",
                name = "Test 3",
                albumCount = 3,
                artworkUrl = null
            )
        )
        val state = ArtistsViewModel.StateUi.Content(isRefreshing = true, items)
        ArtistsScreen(
            state = state,
            onRefresh = {  },
            onBottomReached = {  },
            onArtistClick = {  }
        )
    }
}