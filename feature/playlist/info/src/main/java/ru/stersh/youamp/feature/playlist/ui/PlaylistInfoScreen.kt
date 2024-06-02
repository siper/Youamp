package ru.stersh.youamp.feature.playlist.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.ArtworkMaskColor
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.SongPlayAnimation


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
        onPlayAll = viewModel::playAll,
        onPlayShuffled = viewModel::playShuffled,
        onPlaySong = viewModel::onPlaySong,
        onBackClick = onBackClick
    )
}

@Composable
private fun PlaylistInfoScreen(
    state: PlaylistInfoScreenStateUi,
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
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState
        ) {
            item(
                key = "header",
                contentType = "header"
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Artwork(
                        artworkUrl = state.artworkUrl,
                        placeholder = Icons.Rounded.MusicNote,
                        modifier = Modifier
                            .padding(horizontal = 48.dp)
                            .aspectRatio(1f)
                            .fillMaxWidth()
                    )

                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PlayAllButton(
                            onClick = onPlayAll,
                            modifier = Modifier.weight(0.5f)
                        )
                        PlayShuffledButton(
                            onClick = onPlayShuffled,
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                }
            }
            items(
                items = state.songs,
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
private fun PlaylistSongItem(
    song: PlaylistSongUi,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(text = song.title)
        },
        supportingContent = {
            song.artist?.let {
                Text(text = it)
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
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlaylistInfoScreenPreview() {
    val songs = listOf(
        PlaylistSongUi(
            id = "1",
            title = "Test song",
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
                artworkUrl = null,
                title = "Test",
                songs = songs
            ),
            onPlayAll = {},
            onPlayShuffled = {},
            onPlaySong = {},
            onBackClick = {}
        )
    }
}