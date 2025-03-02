package ru.stersh.youamp.feature.player.mini.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.SingleLineText
import youamp.feature.player.mini.generated.resources.Res
import youamp.feature.player.mini.generated.resources.pause_button_description
import youamp.feature.player.mini.generated.resources.play_button_description

@Composable
fun MiniPlayer(
    windowWidthSizeClass: WindowWidthSizeClass,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: MiniPlayerViewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner)
    val state by viewModel.state.collectAsStateWithLifecycle()

    MiniPlayer(
        state = state,
        windowWidthSizeClass = windowWidthSizeClass,
        onClick = onClick,
        onNext = viewModel::next,
        onPrevious = viewModel::previous,
        onPlayPauseClick = viewModel::playPause,
        modifier = modifier
    )
}

@Composable
private fun MiniPlayer(
    state: StateUi,
    windowWidthSizeClass: WindowWidthSizeClass,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = when (windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    AnimatedVisibility(
        enter = expandVertically(
            animationSpec = tween(
                durationMillis = 100
            )
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                durationMillis = 100
            )
        ),
        visible = !state.invisible,
        modifier = Modifier
            .background(color = containerColor)
            .then(modifier)
    ) {
        if (state.data == null) {
            return@AnimatedVisibility
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = containerColor
        ) {
            when (windowWidthSizeClass) {
                WindowWidthSizeClass.COMPACT -> {
                    PlayerCompact(
                        data = state.data,
                        onClick = onClick,
                        onPlayPauseClick = onPlayPauseClick
                    )
                }

                WindowWidthSizeClass.MEDIUM,
                WindowWidthSizeClass.EXPANDED -> {
                    PlayerExpanded(
                        data = state.data,
                        onClick = onClick,
                        onNext = onNext,
                        onPrevious = onPrevious,
                        onPlayPauseClick = onPlayPauseClick
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerCompact(
    data: PlayerDataUi,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Artwork(
            artworkUrl = data.artworkUrl,
            placeholder = Icons.Rounded.Album,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(start = 24.dp)
                .size(56.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            SingleLineText(
                text = data.title.orEmpty(),
                style = MaterialTheme.typography.titleMedium
            )
            SingleLineText(
                text = data.artist.orEmpty(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { data.progress },
                strokeCap = StrokeCap.Round
            )
            PlayPauseButton(
                modifier = Modifier.size(72.dp),
                isPlaying = data.isPlaying,
                onIsPlayedChanged = { onPlayPauseClick() }
            )
        }
    }
}

@Composable
private fun PlayerExpanded(
    data: PlayerDataUi,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(
                MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .clickable(onClick = onClick)
    ) {
        Artwork(
            artworkUrl = data.artworkUrl,
            placeholder = Icons.Rounded.Album,
            modifier = Modifier
                .size(88.dp)
                .padding(12.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            SingleLineText(
                text = data.title.orEmpty(),
                style = MaterialTheme.typography.titleMedium
            )
            SingleLineText(
                text = data.artist.orEmpty(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onPrevious
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = null
                )
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { data.progress },
                    strokeCap = StrokeCap.Round
                )
                PlayPauseButton(
                    modifier = Modifier.size(72.dp),
                    isPlaying = data.isPlaying,
                    onIsPlayedChanged = { onPlayPauseClick() }
                )
            }
            IconButton(
                onClick = onNext
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun PlayPauseButton(
    isPlaying: Boolean,
    onIsPlayedChanged: (isPlayed: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isPlaying,
        onCheckedChange = onIsPlayedChanged,
        modifier = modifier
    ) {
        if (isPlaying) {
            Icon(
                imageVector = Icons.Rounded.Pause,
                contentDescription = stringResource(Res.string.pause_button_description)
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = stringResource(Res.string.play_button_description)
            )
        }
    }
}

@Composable
@Preview
private fun MiniPlayerPreview() {
    val state = StateUi(
        data = PlayerDataUi(
            title = "Test title",
            artist = "Test artist",
            artworkUrl = "",
            isPlaying = true,
            progress = 0.5f
        ),
        invisible = false
    )
    MiniPlayer(
        state = state,
        windowWidthSizeClass = WindowWidthSizeClass.COMPACT,
        onClick = {},
        onNext = {},
        onPrevious = {},
        onPlayPauseClick = {}
    )
}