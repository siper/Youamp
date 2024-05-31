package ru.stresh.youamp.feature.player.screen.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.feature.player.big.R
import ru.stresh.youamp.core.ui.Artwork
import ru.stresh.youamp.core.ui.BackNavigationButton
import ru.stresh.youamp.core.ui.YouAmpPlayerTheme

@Composable
fun PlayerScreen(
    onBackClick: () -> Unit,
    onPlayQueueClick: () -> Unit
) {
    val viewModel: PlayerScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlayerScreen(
        state = state,
        onSeek = viewModel::seekTo,
        onRepeatModeChanged = viewModel::repeatModeChanged,
        onShuffleModeChanged = viewModel::shuffleModeChanged,
        onFavoriteChanged = viewModel::toggleFavorite,
        onPlayPause = viewModel::playPause,
        onPrevious = viewModel::previous,
        onNext = viewModel::next,
        onBackClick = onBackClick,
        onPlayQueueClick = onPlayQueueClick
    )
}

@Composable
private fun PlayerScreen(
    state: StateUi,
    onSeek: (progress: Float) -> Unit,
    onRepeatModeChanged: (newRepeatMode: RepeatModeUi) -> Unit,
    onShuffleModeChanged: (newShuffleMode: ShuffleModeUi) -> Unit,
    onFavoriteChanged: (isFavorite: Boolean) -> Unit,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onBackClick: () -> Unit,
    onPlayQueueClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
                actions = {
                    PlayerQueueButton(
                        onClick = onPlayQueueClick
                    )
                }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(it)
        ) {

            Artwork(
                artworkUrl = state.artworkUrl,
                placeholder = Icons.Rounded.Album,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = state.title.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )

            Text(
                text = state.artist.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(bottom = 12.dp, start = 24.dp, end = 24.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.currentTime.orEmpty(),
                    style = MaterialTheme.typography.labelLarge
                )
                PlayerSlider(
                    progress = state.progress,
                    onSeek = onSeek,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = state.totalTime.orEmpty(),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledTonalIconButton(
                    onClick = onPrevious,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = stringResource(R.string.previous_song_description)
                    )
                }

                PlayPauseButton(
                    isPlayed = state.isPlaying,
                    onPlayedChanged = onPlayPause
                )

                FilledTonalIconButton(
                    onClick = onNext,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = stringResource(R.string.next_song_description)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                ShuffleButton(
                    shuffleMode = state.shuffleMode,
                    onShuffleModeChanged = onShuffleModeChanged
                )
                RepeatButton(
                    repeatMode = state.repeatMode,
                    onRepeatModeChanged = onRepeatModeChanged
                )
                FavoriteButton(
                    isFavorite = state.isFavorite,
                    onFavoriteChanged = onFavoriteChanged
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
private fun PlayerScreenPreview() {
    YouAmpPlayerTheme {
        val state = StateUi(
            title = "Best song in the world",
            artist = "Best artist in the world",
            isPlaying = false
        )
        PlayerScreen(
            state = state,
            onSeek = {},
            onRepeatModeChanged = {},
            onShuffleModeChanged = {},
            onFavoriteChanged = {},
            onPlayPause = {},
            onPrevious = {},
            onNext = {},
            onBackClick = {},
            onPlayQueueClick = {}
        )
    }
}