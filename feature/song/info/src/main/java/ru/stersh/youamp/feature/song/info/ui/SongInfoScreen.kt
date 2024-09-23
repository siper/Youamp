package ru.stersh.youamp.feature.song.info.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MusicVideo
import androidx.compose.material.icons.rounded.PersonSearch
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.QueuePlayNext
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.SkeletonLayout
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

    LaunchedEffect("load_track_state") {
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
private fun Progress(
    state: SongInfoStateUi
) {
    SkeletonLayout {
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonItem(
                    modifier = Modifier.size(64.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SkeletonItem(
                        modifier = Modifier.size(height = 12.dp, width = 80.dp)
                    )
                    SkeletonItem(
                        modifier = Modifier.size(height = 12.dp, width = 68.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                SkeletonItem(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .size(height = 32.dp, width = 264.dp)
                        .fillMaxWidth()
                )
                SkeletonItem(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .size(height = 32.dp, width = 164.dp)
                        .fillMaxWidth()
                )
                SkeletonItem(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .size(height = 32.dp, width = 200.dp)
                        .fillMaxWidth()
                )
                if (state.showAlbum) {
                    SkeletonItem(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .size(height = 32.dp, width = 300.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: SongInfoStateUi,
    onDismiss: () -> Unit,
    onOpenAlbum: (albumId: String) -> Unit,
    onOpenArtist: (artistId: String) -> Unit,
    onPlay: () -> Unit,
    onPlayNextInQueue: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Artwork(
                artworkUrl = state.artworkUrl,
                placeholder = Icons.Rounded.Album,
                modifier = Modifier.size(64.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                state.title?.let { title ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                state.artist?.let { artist ->
                    Text(
                        text = artist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        MenuItem(
            icon = Icons.Rounded.PlayArrow,
            title = stringResource(R.string.play_title),
            onClick = onPlay
        )
        MenuItem(
            icon = Icons.Rounded.QueuePlayNext,
            title = stringResource(R.string.play_next_in_queue_title),
            onClick = onPlayNextInQueue
        )
        if (state.showAlbum && state.albumId != null) {
            MenuItem(
                icon = Icons.Rounded.MusicVideo,
                title = stringResource(R.string.go_to_album_title),
                onClick = {
                    onOpenAlbum(state.albumId)
                    onDismiss()
                }
            )
        }
        state.artistId?.let { artistId ->
            MenuItem(
                icon = Icons.Rounded.PersonSearch,
                title = stringResource(R.string.go_to_artist_title),
                onClick = {
                    onOpenArtist(artistId)
                    onDismiss()
                }
            )
        }
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
) {
    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        if (state.progress) {
            Progress(
                state = state
            )
        } else {
            Content(
                state = state,
                onDismiss = onDismiss,
                onOpenAlbum = onOpenAlbum,
                onOpenArtist = onOpenArtist,
                onPlay = onPlay,
                onPlayNextInQueue = onPlayNextInQueue
            )
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = remember { RoundedCornerShape(16.dp) }
    Row(
        modifier = Modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 24.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
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