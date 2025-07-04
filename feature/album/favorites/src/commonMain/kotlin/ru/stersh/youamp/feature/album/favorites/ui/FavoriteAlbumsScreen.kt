package ru.stersh.youamp.feature.album.favorites.ui

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
import ru.stersh.youamp.core.ui.AlbumItem
import ru.stersh.youamp.core.ui.AlbumItemDefaults
import ru.stersh.youamp.core.ui.AlbumSkeleton
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.HeaderLayout
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.PullToRefresh
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.isCompactWidth
import youamp.feature.album.favorites.generated.resources.Res
import youamp.feature.album.favorites.generated.resources.favorite_albums_title

@Composable
fun FavoriteAlbumsScreen(
    onAlbumClick: (id: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val viewModel: FavoriteAlbumsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    FavoriteAlbumsScreen(
        state = state,
        onPlayAll = viewModel::playAll,
        onPlayShuffled = viewModel::playShuffled,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onAlbumClick = onAlbumClick,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteAlbumsScreen(
    state: StateUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onAlbumClick: (id: String) -> Unit,
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

                state.data != null && state.data.albums.isEmpty() -> {
                    EmptyLayout(
                        modifier =
                            Modifier.verticalScroll(
                                state = rememberScrollState(),
                            ),
                    )
                }

                state.data?.albums != null -> {
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
                                        text = stringResource(Res.string.favorite_albums_title),
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
                            items = state.data.albums,
                            contentType = { "album" },
                            key = { "album_${it.id}" },
                        ) { album ->
                            AlbumItem(
                                title = album.title,
                                onClick = { onAlbumClick(album.id) },
                                artist = album.artist,
                                artworkUrl = album.artworkUrl,
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
                            .padding(bottom = 28.dp),
                )
            }
            repeat(10) {
                item { AlbumSkeleton() }
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteAlbumsScreenProgressPreview() {
    MaterialTheme {
        FavoriteAlbumsScreen(
            state = StateUi(),
            onPlayAll = {},
            onPlayShuffled = {},
            onRetry = {},
            onRefresh = {},
            onAlbumClick = {},
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun FavoriteAlbumsScreenPreview() {
    MaterialTheme {
        val albums =
            persistentListOf(
                AlbumUi(
                    id = "1",
                    title = "Best alubm in the world 1",
                    artist = "Best artist in the world 1",
                    artworkUrl = null,
                ),
                AlbumUi(
                    id = "2",
                    title = "Best alubm in the world 2",
                    artist = "Best artist in the world 1",
                    artworkUrl = null,
                ),
                AlbumUi(
                    id = "3",
                    title = "Best alubm in the world 3",
                    artist = "Best artist in the world 1",
                    artworkUrl = null,
                ),
                AlbumUi(
                    id = "4",
                    title = "Best alubm in the world 4",
                    artist = "Best artist in the world 1",
                    artworkUrl = null,
                ),
                AlbumUi(
                    id = "5",
                    title = "Best alubm in the world 5",
                    artist = "Best artist in the world 1",
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
                        albums = albums,
                    ),
            )
        FavoriteAlbumsScreen(
            state = state,
            onPlayAll = {},
            onPlayShuffled = {},
            onRetry = {},
            onRefresh = {},
            onAlbumClick = {},
            onBackClick = {},
        )
    }
}
