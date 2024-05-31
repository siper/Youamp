package ru.stresh.youamp.feature.album.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
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
import ru.stresh.youamp.core.ui.Artwork
import ru.stresh.youamp.core.ui.BackNavigationButton
import ru.stresh.youamp.core.ui.PlayAllButton
import ru.stresh.youamp.core.ui.PlayShuffledButton
import ru.stresh.youamp.core.ui.VerticalBigSpacer
import ru.stresh.youamp.core.ui.VerticalSmallSpacer


@Composable
fun AlbumInfoScreen(
    id: String,
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
        onPlaySong = viewModel::onPlaySong,
        onBackClick = onBackClick
    )
}

@Composable
private fun AlbumInfoScreen(
    state: AlbumInfoScreenState,
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
    ) {
        when (state) {
            is AlbumInfoScreenState.Content -> ContentState(
                state = state,
                onPlayAll = onPlayAll,
                onPlayShuffled = onPlayShuffled,
                onPlaySong = onPlaySong,
                modifier = Modifier.padding(it)
            )

            is AlbumInfoScreenState.Progress -> Text(text = "Progress")
            is AlbumInfoScreenState.Error -> Text(text = "Error")
        }
    }
}

@Composable
private fun ContentState(
    state: AlbumInfoScreenState.Content,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onPlaySong: (id: String) -> Unit,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(
            state = scrollState
        )
    ) {
        Artwork(
            artworkUrl = state.coverArtUrl,
            placeholder = Icons.Rounded.Album,
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .aspectRatio(1f)
                .fillMaxWidth()
        )

        VerticalBigSpacer()

        Text(
            text = state.title,
            style = MaterialTheme.typography.titleLarge
        )

        VerticalSmallSpacer()

        val subtitle = if (state.year != null) {
            "${state.artist} · ${state.year}"
        } else {
            state.artist
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        VerticalBigSpacer()

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

        VerticalBigSpacer()

        state.songs.forEach {
            AlbumSongItem(
                song = it,
                onClick = { onPlaySong(it.id) }
            )
        }
    }
}

@Composable
private fun AlbumSongItem(
    song: AlbumSongUi,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(text = song.title)
        },
        trailingContent = {
            Text(text = song.duration)
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview
@Composable
private fun AlbumSongItemPreview() {
    val song = AlbumSongUi(
        id = "1",
        title = "Coolest song in the world",
        duration = "12:00"
    )
    AlbumSongItem(
        song = song,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AlbumInfoScreenPreview() {
    val songs = listOf(
        AlbumSongUi(
            id = "1",
            title = "Test song",
            duration = "2:11"
        ),
        AlbumSongUi(
            id = "2",
            title = "Test song 2",
            duration = "6:23"
        ),
        AlbumSongUi(
            id = "3",
            title = "Test song 3",
            duration = "5:11"
        )
    )
    AlbumInfoScreen(
        state = AlbumInfoScreenState.Content(
            coverArtUrl = null,
            title = "Test",
            artist = "Test",
            year = "2024",
            songs = songs
        ),
        onPlayAll = {},
        onPlayShuffled = {},
        onPlaySong = {},
        onBackClick = {}
    )
}