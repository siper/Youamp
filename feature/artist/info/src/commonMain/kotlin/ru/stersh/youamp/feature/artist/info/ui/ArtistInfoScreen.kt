package ru.stersh.youamp.feature.artist.info.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.CircleArtwork
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.FavoriteButton
import ru.stersh.youamp.core.ui.HeaderLayout
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.SkeletonLayout


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
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(padding)
    ) {
        item(
            key = "header",
            span = { GridItemSpan(2) },
            contentType = "header"
        ) {
            Header(
                state = state,
                onPlayAll = onPlayAll,
                onPlayShuffled = onPlayShuffled,
                onFavoriteChange = onFavoriteChange
            )
        }

        itemsIndexed(
            items = state.albums,
            key = { _, item -> "album_${item.id}" },
            contentType = { _, _ -> "album" }
        ) { index, item ->
            val column = index % 2
            AlbumItem(
                title = item.title,
                artist = item.artist,
                artworkUrl = item.artworkUrl,
                onClick = { onAlbumClick(item.id) },
                modifier = if (column == 0) {
                    Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                } else {
                    Modifier.padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                }
            )
        }
    }
}

@Composable
private fun Header(
    state: ArtistInfoUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onFavoriteChange: (isFavorite: Boolean) -> Unit
) {
    HeaderLayout(
        title = {
            CircleArtwork(
                artworkUrl = state.artworkUrl,
                placeholder = Icons.Rounded.Person,
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .size(160.dp)
            )
            Text(
                text = state.name,
                style = MaterialTheme.typography.titleLarge
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
        }
    )
}

@Composable
private fun Progress(padding: PaddingValues) {
    SkeletonLayout {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding)
        ) {
            item(
                key = "header",
                span = { GridItemSpan(2) },
                contentType = "header"
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SkeletonItem(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(160.dp)
                    )

                    SkeletonItem(
                        modifier = Modifier.size(width = 200.dp, height = 32.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SkeletonItem(
                            modifier = Modifier
                                .height(38.dp)
                                .weight(0.5f)
                        )
                        SkeletonItem(
                            modifier = Modifier
                                .height(38.dp)
                                .weight(0.5f)
                        )
                    }
                }
            }

            itemsIndexed(
                items = (0..10).toList(),
                key = { _, item -> "skeleton_$item" },
                contentType = { _, _ -> "album" }
            ) { index, _ ->
                val column = index % 2
                SkeletonItem(
                    modifier = if (column == 0) {
                        Modifier
                            .height(240.dp)
                            .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    } else {
                        Modifier
                            .height(240.dp)
                            .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    }
                )
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
    MaterialTheme {
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