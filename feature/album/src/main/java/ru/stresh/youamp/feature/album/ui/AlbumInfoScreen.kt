package ru.stresh.youamp.feature.album.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.stresh.youamp.core.ui.Artwork
import ru.stresh.youamp.core.ui.CONTENT_HORIZONTAL_MARGIN
import ru.stresh.youamp.core.ui.HorizontalMediumSpacer
import ru.stresh.youamp.core.ui.VerticalBigSpacer
import ru.stresh.youamp.core.ui.VerticalSmallSpacer


@Preview(showBackground = true)
@Composable
private fun AlbumInfoScreenPreview() {
    AlbumInfoScreen(
        {  },
        "57"
    )
}

@Composable
fun AlbumInfoScreen(
    onBackClick: () -> Unit,
    id: String,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<AlbumInfoViewModel> {
        parametersOf(id)
    }
    val state = viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                    }
                }
            )
        },
        modifier = modifier
    ) {
        when (val stateValue = state.value) {
            is AlbumInfoScreenState.Content -> ContentState(
                state = stateValue,
                viewModel = viewModel,
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
    viewModel: AlbumInfoViewModel,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Artwork(
            artworkUrl = state.coverArtUrl,
            modifier = Modifier.padding(horizontal = 48.dp)
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
                .padding(horizontal = CONTENT_HORIZONTAL_MARGIN)
        ) {
            Button(
                onClick = { viewModel.playAll() },
                modifier = Modifier.weight(0.5f)
            ) {
                Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = "Play icon")
                Text(text = "Play all")
            }
            HorizontalMediumSpacer()
            OutlinedButton(
                onClick = { viewModel.playShuffled() },
                modifier = Modifier.weight(0.5f)
            ) {
                Icon(imageVector = Icons.Rounded.Shuffle, contentDescription = "Shuffle icon")
                Text(text = "Shuffle")
            }
        }

        VerticalBigSpacer()

        state.songs.forEach {
            AlbumSongItem(
                song = it,
                onClick = { viewModel.onPlaySong(it.id) }
            )
        }
    }
}

internal data class AlbumSongUi(
    val id: String,
    val title: String,
    val duration: String
)

@Preview
@Composable
private fun AlbumSongItemPreview() {
    val song = AlbumSongUi(
        id = "1",
        title = "Coolest song in the world",
        duration = "12:00"
    )
    AlbumSongItem(song) {

    }
}

@Composable
internal fun AlbumSongItem(
    song: AlbumSongUi,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp, bottom = 14.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(CONTENT_HORIZONTAL_MARGIN))
            Text(
                text = song.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = song.duration,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.width(CONTENT_HORIZONTAL_MARGIN))
        }
    }
}