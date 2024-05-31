package ru.stresh.youamp.feature.player.queue.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.feature.player.queue.R
import ru.stresh.youamp.core.ui.Artwork
import ru.stresh.youamp.core.ui.ArtworkMaskColor
import ru.stresh.youamp.core.ui.BackNavigationButton
import ru.stresh.youamp.core.ui.DragAndDropLazyColumn
import ru.stresh.youamp.core.ui.SongPlayAnimation
import ru.stresh.youamp.core.ui.YouAmpPlayerTheme

@Composable
fun PlayerQueueScreen(onBackClick: () -> Unit) {
    val viewModel: PlayerQueueViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlayerQueueScreen(
        state = state,
        onSongClick = viewModel::playSong,
        onMoveSong = viewModel::moveSong,
        onBackClick = onBackClick
    )
}

@Composable
private fun PlayerQueueScreen(
    state: StateUi,
    onSongClick: (index: Int) -> Unit,
    onMoveSong: (from: Int, to: Int) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(R.string.play_queue_title))
                },
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                }
            )
        }
    ) { padding ->
        if (state.progress) {
            Box(modifier = Modifier.padding(padding)) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            DragAndDropLazyColumn(
                items = state.songs,
                onSwap = {
                    onMoveSong(it.from, it.to)
                },
                modifier = Modifier.padding(padding)
            ) { index, item ->
                SongItem(
                    song = item,
                    onClick = {
                        onSongClick(index)
                    }
                )
            }
        }
    }
}

@Composable
private fun SongItem(
    song: SongUi,
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
        trailingContent = {
            Icon(
                imageVector = Icons.Rounded.DragHandle,
                contentDescription = "Drag handle"
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
private fun PlayerQueueScreenPreview() {
    YouAmpPlayerTheme {
        val state = StateUi(
            progress = false,
            songs = listOf(
                SongUi(
                    id = "1",
                    title = "Best song in the world",
                    artist = "Best artist in the world",
                    artworkUrl = null,
                    isCurrent = false,
                    isPlaying = false
                ),
                SongUi(
                    id = "1",
                    title = "Best song in the world",
                    artist = null,
                    artworkUrl = null,
                    isCurrent = true,
                    isPlaying = false
                )
            )
        )
        PlayerQueueScreen(
            state = state,
            onSongClick = {},
            onMoveSong = { from, to -> },
            onBackClick = {}
        )
    }
}