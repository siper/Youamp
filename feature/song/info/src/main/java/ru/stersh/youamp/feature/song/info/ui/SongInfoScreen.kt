package ru.stersh.youamp.feature.song.info.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MusicVideo
import androidx.compose.material.icons.rounded.PersonSearch
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.QueuePlayNext
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.Error
import ru.stersh.youamp.core.ui.SongMenu
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.song.info.R


@Composable
fun SongInfoScreen(
    id: String,
    onOpenAlbum: (albumId: String) -> Unit,
    onOpenArtist: (artistId: String) -> Unit,
    onDismiss: () -> Unit,
    showAlbum: Boolean = true
) {
    val viewModel: SongInfoViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect("load_track_state") {
        viewModel.loadSongInfo(songId = id, showAlbum = showAlbum)
    }
    if (state.error) {
        Error(
            onRetry = { viewModel.loadSongInfo(id, showAlbum) },
            modifier = Modifier
                .padding(top = 16.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
        )
    } else {
        SongInfoScreen(
            state = state,
            onDismiss = onDismiss,
            onOpenAlbum = onOpenAlbum,
            onOpenArtist = onOpenArtist,
            onPlay = {
                viewModel.play(id)
                onDismiss()
            },
            onPlayNextInQueue = {
                viewModel.playAfterCurrent(id)
                onDismiss()
            },
            onAddToFavorites = {
                viewModel.addToFavorites(id)
                onDismiss()
            },
            onRemoveFromFavorites = {
                viewModel.removeFromFavorites(id)
                onDismiss()
            },
        )
    }
}

@Composable
private fun SongInfoScreen(
    state: SongInfoStateUi,
    onDismiss: () -> Unit,
    onOpenAlbum: (albumId: String) -> Unit,
    onOpenArtist: (artistId: String) -> Unit,
    onPlay: () -> Unit,
    onPlayNextInQueue: () -> Unit,
    onAddToFavorites: () -> Unit,
    onRemoveFromFavorites: () -> Unit
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

        if (state.favorite) {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = stringResource(R.string.remove_from_favorites),
                    )
                },
                title = {
                    Text(text = stringResource(R.string.remove_from_favorites))
                },
                onClick = onRemoveFromFavorites
            )
        } else {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = stringResource(R.string.add_to_favorites),
                    )
                },
                title = {
                    Text(text = stringResource(R.string.add_to_favorites))
                },
                onClick = onAddToFavorites
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
            onPlayNextInQueue = {},
            onAddToFavorites = {},
            onRemoveFromFavorites = {},
        )
    }
}