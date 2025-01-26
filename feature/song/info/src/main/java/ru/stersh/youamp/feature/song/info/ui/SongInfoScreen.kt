package ru.stersh.youamp.feature.song.info.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MusicVideo
import androidx.compose.material.icons.rounded.PersonSearch
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.QueuePlayNext
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.SongMenu
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.song.info.R
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager


@Composable
fun SongInfoScreen(
    id: String,
    onOpenAlbum: (albumId: String) -> Unit,
    onOpenArtist: (artistId: String) -> Unit,
    onDismiss: () -> Unit,
    showAlbum: Boolean = true
) {
    val apiProvider: ApiProvider = koinInject()
    val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager = koinInject()
    val scope = rememberCoroutineScope()

    var state by remember(showAlbum) { mutableStateOf(SongInfoStateUi(showAlbum = showAlbum)) }

    LaunchedEffect(id) {
        val api = apiProvider.getApi()
        val song = api.getSong(id)

        state = state.copy(
            artworkUrl = api.getCoverArtUrl(song.coverArt),
            title = song.title,
            artist = song.artist,
            artistId = song.artistId,
            albumId = song.albumId,
            progress = false
        )
    }
    SongInfoScreen(
        state = state,
        onDismiss = onDismiss,
        onOpenAlbum = onOpenAlbum,
        onOpenArtist = onOpenArtist,
        onPlay = {
            scope.launch {
                playerQueueAudioSourceManager.playSource(AudioSource.Song(id))
                onDismiss()
            }
        },
        onPlayNextInQueue = {
            scope.launch {
                playerQueueAudioSourceManager.addAfterCurrent(AudioSource.Song(id))
                onDismiss()
            }
        }
    )
}

@Composable
private fun SongInfoScreen(
    state: SongInfoStateUi,
    onDismiss: () -> Unit,
    onOpenAlbum: (albumId: String) -> Unit,
    onOpenArtist: (artistId: String) -> Unit,
    onPlay: () -> Unit,
    onPlayNextInQueue: () -> Unit,
) {
    SongMenu(
        progress = state.progress,
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
                    contentDescription = stringResource(R.string.play_title),
                )
            },
            title = {
                Text(text = stringResource(R.string.play_title))
            },
            onClick = {
                onPlay()
                onDismiss()
            }
        )
        item(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.QueuePlayNext,
                    contentDescription = stringResource(R.string.play_next_in_queue_title),
                )
            },
            title = {
                Text(text = stringResource(R.string.play_next_in_queue_title))
            },
            onClick = {
                onPlayNextInQueue()
                onDismiss()
            }
        )

        if (state.showAlbum && state.albumId != null) {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.MusicVideo,
                        contentDescription = stringResource(R.string.go_to_album_title)
                    )
                },
                title = {
                    Text(text = stringResource(R.string.go_to_album_title))
                },
                onClick = {
                    onOpenAlbum(state.albumId)
                    onDismiss()
                }
            )
        }

        state.artistId?.let { artistId ->
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.PersonSearch,
                        contentDescription = stringResource(R.string.go_to_artist_title)
                    )
                },
                title = {
                    Text(text = stringResource(R.string.go_to_artist_title))
                },
                onClick = {
                    onOpenArtist(artistId)
                    onDismiss()
                }
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
private fun AlbumInfoScreenPreview() {
    YouampPlayerTheme {
        SongInfoScreen(
            state = SongInfoStateUi(
                title = "Test song",
                artistId = "1",
                albumId = "1",
                artist = "Test artist",
                progress = false
            ),
            onDismiss = {},
            onPlay = {},
            onOpenAlbum = {},
            onOpenArtist = {},
            onPlayNextInQueue = {}
        )
    }
}