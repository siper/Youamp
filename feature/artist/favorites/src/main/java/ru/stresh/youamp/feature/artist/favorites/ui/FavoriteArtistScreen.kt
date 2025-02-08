package ru.stresh.youamp.feature.artist.favorites.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.ArtistItem
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.HeaderLayout
import ru.stersh.youamp.core.ui.HeaderTitle
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.feature.artist.favorites.R

@Composable
fun FavoriteArtistsScreen(
    onArtistClick: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: FavoriteArtistViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    FavoriteArtistsScreen(
        state = state,
        onPlayAll = viewModel::playAll,
        onPlayShuffled = viewModel::playShuffled,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onArtistClick = onArtistClick,
        onBackClick = onBackClick
    )
}

@Composable
private fun FavoriteArtistsScreen(
    state: StateUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onArtistClick: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                }
            )
        }
    ) {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(it)
        ) {
            when {
                state.progress -> {
                    Progress()
                }

                state.error -> {
                    ErrorLayout(onRetry = onRetry)
                }

                state.data != null && state.data.artists.isEmpty() -> {
                    EmptyLayout(
                        modifier = Modifier.verticalScroll(
                            state = rememberScrollState()
                        )
                    )
                }

                state.data?.artists != null -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item(
                            contentType = "header",
                            key = "header",
                            span = { GridItemSpan(2) }
                        ) {
                            HeaderLayout(
                                title = {
                                    HeaderTitle(text = stringResource(R.string.favorite_artists_title))
                                },
                                actions = {
                                    PlayAllButton(
                                        onClick = onPlayAll
                                    )
                                    PlayShuffledButton(
                                        onClick = onPlayShuffled
                                    )
                                }
                            )
                        }
                        items(
                            items = state.data.artists,
                            contentType = { "artist" },
                            key = { "artist_${it.id}" }
                        ) { album ->
                            ArtistItem(
                                name = album.name,
                                onClick = { onArtistClick(album.id) },
                                artworkUrl = album.artworkUrl
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Progress() {
    SkeletonLayout {
        repeat(10) {
            ListItem(
                headlineContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SkeletonItem(modifier = Modifier.size(width = 130.dp, height = 16.dp))
                        SkeletonItem(modifier = Modifier.size(width = 200.dp, height = 16.dp))
                    }
                },
                leadingContent = {
                    SkeletonItem(modifier = Modifier.size(48.dp))
                }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FavoriteArtistsScreenPreview() {
    MaterialTheme {
        val albums = persistentListOf(
            ArtistUi(
                id = "1",
                name = "Artist 1",
                artworkUrl = null
            ),
            ArtistUi(
                id = "2",
                name = "Artist 2",
                artworkUrl = null
            ),
            ArtistUi(
                id = "3",
                name = "Artist 3",
                artworkUrl = null
            ),
            ArtistUi(
                id = "4",
                name = "Artist 4",
                artworkUrl = null
            ),
            ArtistUi(
                id = "5",
                name = "Artist 5",
                artworkUrl = null
            ),
        )
        val state = StateUi(
            progress = false,
            isRefreshing = false,
            error = false,
            data = DataUi(
                artists = albums
            )
        )
        FavoriteArtistsScreen(
            state = state,
            onPlayAll = {},
            onPlayShuffled = {},
            onRetry = {},
            onRefresh = {},
            onArtistClick = {},
            onBackClick = {}
        )
    }
}