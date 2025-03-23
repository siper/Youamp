package ru.stersh.youamp.feature.album.info.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.FavoriteButton
import ru.stersh.youamp.core.ui.HeaderLayout
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.SkeletonLayout


@Composable
fun AlbumInfoScreen(
    id: String,
    onOpenSongInfo: (songId: String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<AlbumInfoViewModel> {
        parametersOf(id)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    AlbumInfoScreen(
        state = state,
        onPlayAll = viewModel::playAll,
        onPlayShuffled = viewModel::playShuffled,
        onFavoriteChange = viewModel::onFavoriteChange,
        onPlaySong = onOpenSongInfo,
        onRetry = viewModel::retry,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumInfoScreen(
    state: AlbumInfoStateUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onFavoriteChange: (isFavorite: Boolean) -> Unit,
    onPlaySong: (id: String) -> Unit,
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
    ) {
        when {
            state.progress -> {
                Progress(padding = it)
            }

            state.error -> {
                ErrorLayout(
                    onRetry = onRetry,
                    modifier = Modifier.padding(it)
                )
            }

            state.content != null -> {
                ContentState(
                    state = state.content,
                    onPlayAll = onPlayAll,
                    onPlayShuffled = onPlayShuffled,
                    onFavoriteChange = onFavoriteChange,
                    onPlaySong = onPlaySong,
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}

@Composable
private fun Progress(padding: PaddingValues) {
    SkeletonLayout {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SkeletonItem(
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .aspectRatio(1f)
                    .fillMaxWidth()
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SkeletonItem(
                    modifier = Modifier.size(width = 200.dp, height = 32.dp)
                )
                SkeletonItem(
                    modifier = Modifier.size(width = 160.dp, height = 24.dp)
                )
            }

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

            Column {
                repeat(5) {
                    ListItem(
                        headlineContent = {
                            SkeletonItem(modifier = Modifier.size(width = 130.dp, height = 24.dp))
                        },
                        trailingContent = {
                            SkeletonItem(modifier = Modifier.size(width = 30.dp, height = 16.dp))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentState(
    state: AlbumInfoUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onFavoriteChange: (isFavorite: Boolean) -> Unit,
    onPlaySong: (id: String) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        item(
            key = "header",
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
            items = state.songs,
            contentType = { "song" },
            key = { "song_${it.id}" }
        ) {
            AlbumSongItem(
                song = it,
                onClick = { onPlaySong(it.id) }
            )
        }
    }
}

@Composable
private fun Header(
    state: AlbumInfoUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onFavoriteChange: (isFavorite: Boolean) -> Unit
) {
    HeaderLayout(
        image = {
            Artwork(
                artworkUrl = state.artworkUrl,
                placeholder = Icons.Rounded.Album,
                modifier = Modifier
                    .aspectRatio(1f)
            )
        },
        title = {
            Text(
                text = state.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        },
        subtitle = {
            val subtitle = if (state.year != null) {
                "${state.artist} · ${state.year}"
            } else {
                state.artist
            }
            Text(
                text = subtitle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
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
        }
    )
}

@Composable
private fun AlbumSongItem(
    song: AlbumSongUi,
    onClick: () -> Unit
) {
    ListItem(
        leadingContent = {
            song.track?.let { Text(text = it.toString()) }
        },
        headlineContent = {
            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            song.artist?.let { 
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        trailingContent = {
            Text(text = song.duration)
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview
@Composable
private fun AlbumInfoScreenPreview() {
    val songs = persistentListOf(
        AlbumSongUi(
            id = "1",
            track = 1,
            title = "Test song",
            duration = "2:11",
            artist = "Test artist"

        ),
        AlbumSongUi(
            id = "2",
            track = 2,
            title = "Test song 2",
            artist = "Someone else",
            duration = "6:23",
        ),
        AlbumSongUi(
            id = "3",
            track = 3,
            title = "Test song 3",
            artist = "Test artist",
            duration = "5:11"
        )
    )
    AlbumInfoScreen(
        state = AlbumInfoStateUi(
            progress = false,
            error = false,
            content = AlbumInfoUi(
                artworkUrl = null,
                isFavorite = true,
                title = "Test",
                artist = "Test",
                year = "2024",
                songs = songs
            )
        ),
        onPlayAll = {},
        onPlayShuffled = {},
        onFavoriteChange = {},
        onPlaySong = {},
        onRetry = {},
        onBackClick = {}
    )
}