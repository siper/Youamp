package ru.stersh.youamp.feature.playlist.info.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import ru.stersh.youamp.core.ui.ArtworkMaskColor
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.HeaderLayout
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.SongPlayAnimation
import ru.stersh.youamp.core.ui.isCompactWidth


@Composable
fun PlaylistInfoScreen(
    id: String,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<PlaylistInfoViewModel> {
        parametersOf(id)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlaylistInfoScreen(
        state = state,
        onRetry = viewModel::retry,
        onPlayAll = viewModel::playAll,
        onPlayShuffled = viewModel::playShuffled,
        onPlaySong = viewModel::onPlaySong,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistInfoScreen(
    state: PlaylistInfoScreenStateUi,
    onRetry: () -> Unit,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onPlaySong: (id: String) -> Unit,
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
                ErrorLayout(onRetry = onRetry)
            }

            state.playlistInfo != null -> {
                Content(
                    padding = padding,
                    info = state.playlistInfo,
                    onPlayAll = onPlayAll,
                    onPlayShuffled = onPlayShuffled,
                    onPlaySong = onPlaySong
                )
            }
        }
    }
}

@Composable
private fun Content(
    padding: PaddingValues,
    info: PlaylistInfoUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onPlaySong: (id: String) -> Unit
) {
    if (info.songs.isEmpty()) {
        Column(modifier = Modifier.padding(padding)) {
            Header(
                info = info,
                onPlayAll = onPlayAll,
                onPlayShuffled = onPlayShuffled
            )
            EmptyLayout(modifier = Modifier.weight(1f))
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            item(
                key = "header",
                contentType = "header"
            ) {
                Header(
                    info = info,
                    onPlayAll = onPlayAll,
                    onPlayShuffled = onPlayShuffled
                )
            }
            items(
                items = info.songs,
                contentType = { "song" }
            ) {
                PlaylistSongItem(
                    song = it,
                    onClick = { onPlaySong(it.id) }
                )
            }
        }
    }
}

@Composable
private fun Header(
    info: PlaylistInfoUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit
) {
    HeaderLayout(
        image = {
            Artwork(
                artworkUrl = info.artworkUrl,
                placeholder = Icons.Rounded.MusicNote,
                modifier = Modifier
                    .then(
                        if (isCompactWidth) {
                            Modifier
                                .padding(horizontal = 48.dp)
                                .fillMaxWidth()
                        } else {
                            Modifier
                        }
                    )
                    .aspectRatio(1f)
            )
        },
        title = {
            Text(
                text = info.title,
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
        }
    )
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

            Column {
                repeat(5) {
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
    }
}

@Composable
private fun PlaylistSongItem(
    song: PlaylistSongUi,
    onClick: () -> Unit
) {
    ListItem(
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
        leadingContent = {
            Artwork(
                artworkUrl = song.artworkUrl,
                placeholder = Icons.Rounded.MusicNote,
                modifier = Modifier.size(48.dp)
            )
            if (song.isCurrent) {
                SongPlayAnimation(
                    isPlaying = song.isPlaying,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = ArtworkMaskColor,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(12.dp)
                )
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview
@Composable
private fun PlaylistInfoScreenPreview() {
    val songs = persistentListOf(
        PlaylistSongUi(
            id = "1",
            title = "Test song with veeeeeeeeeeeeery long title",
            artist = "Cool artist",
            artworkUrl = null,
            isCurrent = false,
            isPlaying = false
        ),
        PlaylistSongUi(
            id = "2",
            title = "Test song 2",
            artist = "Cool artist",
            artworkUrl = null,
            isCurrent = false,
            isPlaying = false
        ),
        PlaylistSongUi(
            id = "3",
            title = "Test song 3",
            artist = null,
            artworkUrl = null,
            isCurrent = true,
            isPlaying = false
        )
    )
    MaterialTheme {
        PlaylistInfoScreen(
            state = PlaylistInfoScreenStateUi(
                progress = false,
                playlistInfo = PlaylistInfoUi(
                    artworkUrl = null,
                    title = "Test",
                    songs = songs
                )
            ),
            onRetry = {},
            onPlayAll = {},
            onPlayShuffled = {},
            onPlaySong = {},
            onBackClick = {}
        )
    }
}