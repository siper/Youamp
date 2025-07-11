package ru.stersh.youamp.feature.artist.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.AlbumItemDefaults
import ru.stersh.youamp.core.ui.ArtistItem
import ru.stersh.youamp.core.ui.ArtistSkeleton
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.HeaderLayout
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.PullToRefresh
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.isCompactWidth
import youamp.feature.artist.favorites.generated.resources.Res
import youamp.feature.artist.favorites.generated.resources.favorite_artists_title

@Composable
fun FavoriteArtistsScreen(
    onArtistClick: (id: String) -> Unit,
    onBackClick: () -> Unit,
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
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteArtistsScreen(
    state: StateUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onArtistClick: (id: String) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
            )
        },
    ) {
        PullToRefresh(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(it),
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
                        modifier =
                            Modifier.verticalScroll(
                                state = rememberScrollState(),
                            ),
                    )
                }

                state.data?.artists != null -> {
                    LazyVerticalGrid(
                        columns =
                            if (isCompactWidth) {
                                GridCells.Fixed(2)
                            } else {
                                GridCells.Adaptive(AlbumItemDefaults.Width)
                            },
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        item(
                            contentType = "header",
                            key = "header",
                            span = { GridItemSpan(maxCurrentLineSpan) },
                        ) {
                            HeaderLayout(
                                title = {
                                    Text(
                                        text = stringResource(Res.string.favorite_artists_title),
                                        modifier = Modifier.singleHeader(),
                                    )
                                },
                                actions = {
                                    PlayAllButton(
                                        onClick = onPlayAll,
                                    )
                                    PlayShuffledButton(
                                        onClick = onPlayShuffled,
                                    )
                                },
                            )
                        }
                        items(
                            items = state.data.artists,
                            contentType = { "artist" },
                            key = { "artist_${it.id}" },
                        ) { artist ->
                            ArtistItem(
                                name = artist.name,
                                onClick = { onArtistClick(artist.id) },
                                artworkUrl = artist.artworkUrl,
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
    SkeletonLayout(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns =
                if (isCompactWidth) {
                    GridCells.Fixed(2)
                } else {
                    GridCells.Adaptive(AlbumItemDefaults.Width)
                },
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                HeaderLayout(
                    title = {
                        Column(
                            modifier = Modifier.singleHeader(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            SkeletonItem(
                                modifier =
                                    Modifier
                                        .size(
                                            400.dp,
                                            32.dp,
                                        ),
                            )
                            SkeletonItem(
                                modifier =
                                    Modifier
                                        .size(
                                            200.dp,
                                            32.dp,
                                        ),
                            )
                        }
                    },
                    actions = {
                        SkeletonItem(
                            shape = CircleShape,
                            modifier = Modifier.size(64.dp),
                        )
                        SkeletonItem(
                            shape = CircleShape,
                            modifier = Modifier.size(64.dp),
                        )
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                )
            }
            repeat(10) {
                item { ArtistSkeleton() }
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteArtistsScreenProgressPreview() {
    MaterialTheme {
        FavoriteArtistsScreen(
            state = StateUi(),
            onPlayAll = {},
            onPlayShuffled = {},
            onRetry = {},
            onRefresh = {},
            onArtistClick = {},
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun FavoriteArtistsScreenPreview() {
    MaterialTheme {
        val albums =
            persistentListOf(
                ArtistUi(
                    id = "1",
                    name = "Artist 1",
                    artworkUrl = null,
                ),
                ArtistUi(
                    id = "2",
                    name = "Artist 2",
                    artworkUrl = null,
                ),
                ArtistUi(
                    id = "3",
                    name = "Artist 3",
                    artworkUrl = null,
                ),
                ArtistUi(
                    id = "4",
                    name = "Artist 4",
                    artworkUrl = null,
                ),
                ArtistUi(
                    id = "5",
                    name = "Artist 5",
                    artworkUrl = null,
                ),
            )
        val state =
            StateUi(
                progress = false,
                isRefreshing = false,
                error = false,
                data =
                    DataUi(
                        artists = albums,
                    ),
            )
        FavoriteArtistsScreen(
            state = state,
            onPlayAll = {},
            onPlayShuffled = {},
            onRetry = {},
            onRefresh = {},
            onArtistClick = {},
            onBackClick = {},
        )
    }
}
