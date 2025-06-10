package ru.stersh.youamp.feature.artist.info.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.stersh.youamp.core.ui.AlbumItem
import ru.stersh.youamp.core.ui.AlbumItemDefaults
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.CircleArtwork
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.FavoriteButton
import ru.stersh.youamp.core.ui.HeaderLayout
import ru.stersh.youamp.core.ui.LocalWindowSizeClass
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.core.ui.isCompactWidth


@Composable
fun ArtistInfoScreen(
    id: String,
    onAlbumClick: (albumId: String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<ArtistInfoViewModel> {
        parametersOf(id)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    ArtistInfoScreen(
        state = state,
        onPlayAll = viewModel::playAll,
        onPlayShuffled = viewModel::playShuffled,
        onFavoriteChange = viewModel::onFavoriteChange,
        onAlbumClick = onAlbumClick,
        onRetry = viewModel::retry,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtistInfoScreen(
    state: ArtistInfoStateUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onFavoriteChange: (isFavorite: Boolean) -> Unit,
    onAlbumClick: (albumId: String) -> Unit,
    onRetry: () -> Unit,
    onBackClick: () -> Unit,
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
    ) { padding ->

        when {
            state.progress -> {
                Progress(padding = padding)
            }

            state.error -> {
                ErrorLayout(
                    onRetry = onRetry,
                    modifier = Modifier.padding(padding)
                )
            }

            state.content != null -> {
                Content(
                    padding = padding,
                    state = state.content,
                    onPlayAll = onPlayAll,
                    onPlayShuffled = onPlayShuffled,
                    onFavoriteChange = onFavoriteChange,
                    onAlbumClick = onAlbumClick
                )
            }
        }
    }
}

@Composable
private fun Content(
    padding: PaddingValues,
    state: ArtistInfoUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onFavoriteChange: (isFavorite: Boolean) -> Unit,
    onAlbumClick: (albumId: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        modifier = Modifier.padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(
            key = "header",
            span = { GridItemSpan(maxLineSpan) },
            contentType = "header"
        ) {
            Header(
                state = state,
                onPlayAll = onPlayAll,
                onPlayShuffled = onPlayShuffled,
                onFavoriteChange = onFavoriteChange
            )
        }

        items(
            items = state.albums,
            key = { "album_${it.id}" },
            contentType = { "album" }
        ) {
            AlbumItem(
                title = it.title,
                artist = it.artist,
                artworkUrl = it.artworkUrl,
                onClick = { onAlbumClick(it.id) }
            )
        }
    }
}

@Composable
private fun Header(
    state: ArtistInfoUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onFavoriteChange: (isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowWidthSizeClass = LocalWindowSizeClass.current.widthSizeClass
    HeaderLayout(
        image = {
            CircleArtwork(
                artworkUrl = state.artworkUrl,
                placeholder = Icons.Rounded.Person,
                modifier = if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
                    Modifier.size(160.dp)
                } else {
                    Modifier
                }
            )
        },
        title = {
            Text(
                text = state.name,
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            PlayAllButton(
                onClick = onPlayAll
            )
            PlayShuffledButton(
                onClick = onPlayShuffled
            )
            FavoriteButton(
                isFavorite = state.isFavorite,
                onChange = onFavoriteChange
            )
        },
        modifier = modifier
    )
}

@Composable
private fun Progress(padding: PaddingValues) {
    SkeletonLayout {
        Column(modifier = Modifier.padding(padding)) {
            HeaderLayout(
                image = {
                    SkeletonItem(
                        modifier = if (LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Compact) {
                            Modifier.size(160.dp)
                        } else {
                            Modifier.fillMaxWidth()
                                .aspectRatio(1f)
                        }.clip(CircleShape)
                    )
                },
                title = {
                    if (LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Compact) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            SkeletonItem(
                                modifier = Modifier.size(280.dp, 32.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                            )
                        }
                    } else {
                        SkeletonItem(
                            modifier = Modifier.size(300.dp, 48.dp)
                        )
                    }
                },
                subtitle = {
                    if (LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Compact) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            SkeletonItem(
                                modifier = Modifier.size(220.dp, 24.dp)
                                    .fillMaxWidth()
                                    .padding(top = 4.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    } else {
                        SkeletonItem(
                            modifier = Modifier
                                .size(220.dp, 24.dp)
                                .padding(top = 4.dp)
                        )
                    }
                },
                actions = {
                    repeat(2) {
                        SkeletonItem(
                            modifier = Modifier.size(64.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            )
            LazyVerticalGrid(
                columns = if (isCompactWidth) {
                    GridCells.Fixed(2)
                } else {
                    GridCells.Adaptive(AlbumItemDefaults.Width)
                },
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                repeat(20) {
                    item {
                        SkeletonItem(
                            modifier = Modifier
                                .size(AlbumItemDefaults.Width, 220.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ArtistInfoScreenPreview() {
    val albums = persistentListOf(
        AlbumUi(
            id = "1",
            title = "Test album",
            artist = null,
            artworkUrl = null
        ),
        AlbumUi(
            id = "2",
            title = "Test album 2",
            artist = null,
            artworkUrl = null
        ),
        AlbumUi(
            id = "3",
            title = "Test album 3",
            artist = null,
            artworkUrl = null
        )
    )
    YouampPlayerTheme {
        ArtistInfoScreen(
            state = ArtistInfoStateUi(
                progress = false,
                error = false,
                content = ArtistInfoUi(
                    artworkUrl = null,
                    isFavorite = false,
                    name = "Artist",
                    albums = albums
                )
            ),
            onPlayAll = {},
            onAlbumClick = {},
            onPlayShuffled = {},
            onFavoriteChange = {},
            onRetry = {},
            onBackClick = {}
        )
    }
}