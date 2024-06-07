package ru.stersh.youamp.feature.artists.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.YouAmpPlayerTheme


@Composable
fun ArtistsScreen(
    viewModelStoreOwner: ViewModelStoreOwner,
    onArtistClick: (id: String) -> Unit
) {
    val viewModel: ArtistsViewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner)

    val state by viewModel.state.collectAsStateWithLifecycle()

    ArtistsScreen(
        state = state,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onArtistClick = onArtistClick
    )
}

@Composable
private fun ArtistsScreen(
    state: ArtistsStateUi,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onArtistClick: (id: String) -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    if (pullRefreshState.isRefreshing) {
        onRefresh()
    }

    if (pullRefreshState.isRefreshing && !state.isRefreshing) {
        pullRefreshState.endRefresh()
    }

    val listState = rememberLazyGridState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullRefreshState.nestedScrollConnection)
    ) {
        when {
            state.progress -> {
                Progress()
            }

            state.error -> {
                ErrorLayout(onRetry = onRetry)
            }

            state.items.isEmpty() -> {
                EmptyLayout()
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.items
                    ) { album ->
                        ArtistItem(
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
        }
    }
}

@Composable
private fun Progress() {
    SkeletonLayout {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = (0..12).toList()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SkeletonItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = CircleShape
                    )
                    SkeletonItem(
                        modifier = Modifier.size(width = 80.dp, height = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ArtistItem(
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
        Artwork(
            artworkUrl = artist.artworkUrl,
            placeholder = Icons.Rounded.Person,
            shape = circleShape,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
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
private fun ArtistsScreenPreview() {
    YouAmpPlayerTheme {
        val items = listOf(
            ArtistUi(
                id = "1",
                name = "Test",
                artworkUrl = null
            ),
            ArtistUi(
                id = "2",
                name = "Test 2",
                artworkUrl = null
            ),
            ArtistUi(
                id = "3",
                name = "Test 3",
                artworkUrl = null
            ),
            ArtistUi(
                id = "4",
                name = "Test 4",
                artworkUrl = null
            ),
            ArtistUi(
                id = "5",
                name = "Test 5",
                artworkUrl = null
            )
        )
        val state = ArtistsStateUi(
            progress = false,
            isRefreshing = true,
            error = false,
            items = items
        )
        ArtistsScreen(
            state = state,
            onRetry = {},
            onRefresh = {},
            onArtistClick = {}
        )
    }
}