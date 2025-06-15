package ru.stersh.youamp.feature.song.info.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddToQueue
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.Error
import ru.stersh.youamp.core.ui.SongMenu
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import youamp.feature.song.info.generated.resources.Res
import youamp.feature.song.info.generated.resources.add_to_favorites
import youamp.feature.song.info.generated.resources.add_to_queue_last_title
import youamp.feature.song.info.generated.resources.add_to_queue_next_title
import youamp.feature.song.info.generated.resources.go_to_album_title
import youamp.feature.song.info.generated.resources.go_to_artist_title
import youamp.feature.song.info.generated.resources.play_title
import youamp.feature.song.info.generated.resources.remove_from_favorites

@Composable
fun SongInfoScreen(
    id: String,
    showAlbum: Boolean = true,
    onOpenAlbum: (albumId: String) -> Unit,
    onOpenArtist: (artistId: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val viewModel: SongInfoViewModel =
        koinViewModel<SongInfoViewModel> {
            parametersOf(
                id,
                showAlbum,
            )
        }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dismiss.collect {
            onDismiss()
        }
    }

    if (state.error) {
        Error(
            onRetry = viewModel::retry,
            modifier =
                Modifier
                    .padding(top = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
        )
    } else {
        SongInfoScreen(
            state = state,
            onOpenAlbum = onOpenAlbum,
            onOpenArtist = onOpenArtist,
            onPlay = {
                viewModel.play(id)
            },
            onAddToQueueNext = {
                viewModel.addToQueueNext(id)
            },
            onAddToQueueLast = {
                viewModel.addToQueueLast(id)
            },
            onAddToFavorites = {
                viewModel.addToFavorites(id)
            },
            onRemoveFromFavorites = {
                viewModel.removeFromFavorites(id)
            },
        )
    }
}

@Composable
private fun SongInfoScreen(
    state: StateUi,
    onOpenAlbum: (albumId: String) -> Unit,
    onOpenArtist: (artistId: String) -> Unit,
    onPlay: () -> Unit,
    onAddToQueueNext: () -> Unit,
    onAddToQueueLast: () -> Unit,
    onAddToFavorites: () -> Unit,
    onRemoveFromFavorites: () -> Unit,
) {
    SongMenu(
        progress = state.progress,
        artwork = {
            Artwork(
                artworkUrl = state.artworkUrl,
                placeholder = Icons.Rounded.Album,
                modifier = Modifier.fillMaxSize(),
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
        },
    ) {
        item(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = stringResource(Res.string.play_title),
                )
            },
            title = {
                Text(text = stringResource(Res.string.play_title))
            },
            onClick = {
                onPlay()
            },
        )
        item(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.AddToQueue,
                    contentDescription = stringResource(Res.string.add_to_queue_last_title),
                )
            },
            title = {
                Text(text = stringResource(Res.string.add_to_queue_last_title))
            },
            onClick = onAddToQueueLast,
        )
        item(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.QueuePlayNext,
                    contentDescription = stringResource(Res.string.add_to_queue_next_title),
                )
            },
            title = {
                Text(text = stringResource(Res.string.add_to_queue_next_title))
            },
            onClick = onAddToQueueNext,
        )

        if (state.showAlbum && state.albumId != null) {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.MusicVideo,
                        contentDescription = stringResource(Res.string.go_to_album_title),
                    )
                },
                title = {
                    Text(text = stringResource(Res.string.go_to_album_title))
                },
                onClick = {
                    onOpenAlbum(state.albumId)
                },
            )
        }

        state.artistId?.let { artistId ->
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.PersonSearch,
                        contentDescription = stringResource(Res.string.go_to_artist_title),
                    )
                },
                title = {
                    Text(text = stringResource(Res.string.go_to_artist_title))
                },
                onClick = {
                    onOpenArtist(artistId)
                },
            )
        }

        if (state.favorite) {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = stringResource(Res.string.remove_from_favorites),
                    )
                },
                title = {
                    Text(text = stringResource(Res.string.remove_from_favorites))
                },
                onClick = onRemoveFromFavorites,
            )
        } else {
            item(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = stringResource(Res.string.add_to_favorites),
                    )
                },
                title = {
                    Text(text = stringResource(Res.string.add_to_favorites))
                },
                onClick = onAddToFavorites,
            )
        }
    }
}

@Composable
@Preview
private fun AlbumInfoScreenPreview() {
    YouampPlayerTheme {
        SongInfoScreen(
            state =
                StateUi(
                    title = "Test song",
                    artistId = "1",
                    albumId = "1",
                    artist = "Test artist",
                    progress = false,
                ),
            onPlay = {},
            onOpenAlbum = {},
            onOpenArtist = {},
            onAddToQueueNext = {},
            onAddToFavorites = {},
            onRemoveFromFavorites = {},
            onAddToQueueLast = {},
        )
    }
}
