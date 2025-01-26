package ru.stersh.youamp.feature.player.queue.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RemoveFromQueue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.SongMenu
import ru.stersh.youamp.feature.player.queue.R

@Composable
internal fun PlayQueueSongMenu(
    state: MenuSongStateUi,
    onPlaySong: (index: Int) -> Unit,
    onRemoveSong: (index: Int) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        dragHandle = {},
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismiss
    ) {
        SongMenu(
            progress = false,
            artwork = {
                Artwork(
                    artworkUrl = state.artworkUrl,
                    placeholder = Icons.Rounded.Album,
                    modifier = Modifier.fillMaxSize()
                )
            },
            title = {
                state.title?.let {
                    Text(text = it)
                }
            },
            artist = {
                state.artist?.let {
                    Text(text = it)
                }
            }
        ) {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = stringResource(R.string.play_song_title),
                    )
                },
                title = {
                    Text(text = stringResource(R.string.play_song_title))
                },
                onClick = {
                    onPlaySong(state.index)
                    onDismiss()
                }
            )

            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.RemoveFromQueue,
                        contentDescription = stringResource(R.string.remove_from_queue_title),
                    )
                },
                title = {
                    Text(text = stringResource(R.string.remove_from_queue_title))
                },
                onClick = {
                    onRemoveSong(state.index)
                    onDismiss()
                }
            )
        }
    }
}