package ru.stresh.youamp.feature.player.screen.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stresh.youamp.core.ui.Artwork

@Composable
fun PlayerScreen(onBackClick: () -> Unit) {
    PlayerScreen(
        onBackClick = onBackClick,
        viewModel = koinViewModel()
    )
}

@Composable
private fun PlayerScreen(
    onBackClick: () -> Unit,
    viewModel: PlayerScreenViewModel
) {
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
        }
    ) {
        Surface(
            modifier = Modifier.padding(it),
            color = MaterialTheme.colorScheme.background
        ) {

            val artworkUrl = viewModel.artworkUrl.collectAsStateWithLifecycle()
            val title = viewModel.title.collectAsStateWithLifecycle()
            val artist = viewModel.artist.collectAsStateWithLifecycle()
            val isPlaying = viewModel.isPlaying.collectAsStateWithLifecycle()
            val progress = viewModel.progress.collectAsStateWithLifecycle()

            val currentTime = viewModel.currentTime.collectAsStateWithLifecycle()
            val totalTime = viewModel.totalTime.collectAsStateWithLifecycle()

            val repeatMode = remember {
                mutableStateOf(RepeatMode.Disabled)
            }

            val shuffleMode = remember {
                mutableStateOf(ShuffleMode.Disabled)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Artwork(
                    artworkUrl = artworkUrl.value,
                    modifier = Modifier.padding(horizontal = 48.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = title.value.orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    minLines = 2,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp)
                )

                Text(
                    text = artist.value.orEmpty(),
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
                        text = currentTime.value.orEmpty(),
                        style = MaterialTheme.typography.labelLarge
                    )
                    PlayerSlider(
                        progress = progress.value,
                        onSeek = {
                            viewModel.seekTo(it)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                    Text(
                        text = totalTime.value.orEmpty(),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(70.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RepeatButton(
                        repeatMode = repeatMode.value,
                        onRepeatModeChanged = { repeatMode.value = it }
                    )
                    Spacer(
                        modifier = Modifier.weight(0.25f)
                    )
                    ArrowButton(
                        direction = ArrowDirection.Previous,
                        onClick = { viewModel.previous() }
                    )
                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                    PlayPauseButton(
                        isPlayed = isPlaying,
                        onPlayedChanged = { viewModel.playPause() }
                    )
                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                    ArrowButton(
                        direction = ArrowDirection.Next,
                        onClick = { viewModel.next() }
                    )
                    Spacer(
                        modifier = Modifier.weight(0.25f)
                    )
                    ShuffleButton(
                        shuffleMode = shuffleMode.value,
                        onShuffleModeChanged = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerSlider(
    progress: Float,
    onSeek: (progress: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val sliderPosition = remember(progress) { mutableFloatStateOf(progress) }
    val tempSliderPosition = remember { mutableFloatStateOf(progress) }
    val interactionSource = remember { MutableInteractionSource() }
    val isDragged = interactionSource.collectIsDraggedAsState()

    Slider(
        value = if (isDragged.value) {
            tempSliderPosition.floatValue
        } else {
            sliderPosition.floatValue
        },
        onValueChange = {
            sliderPosition.floatValue = it
            tempSliderPosition.floatValue = it
        },
        onValueChangeFinished = {
            sliderPosition.floatValue = tempSliderPosition.floatValue
            onSeek(tempSliderPosition.floatValue)
        },
        interactionSource = interactionSource,
        modifier = modifier
    )
}

@Composable
private fun ShuffleButton(
    shuffleMode: ShuffleMode,
    onShuffleModeChanged: (newShuffleMode: ShuffleMode) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = {
            val newShuffleMode = when (shuffleMode) {
                ShuffleMode.Enabled -> ShuffleMode.Disabled
                ShuffleMode.Disabled -> ShuffleMode.Enabled
            }
            onShuffleModeChanged.invoke(newShuffleMode)
        },
        modifier = modifier
    ) {
        val tint = when (shuffleMode) {
            ShuffleMode.Disabled -> MaterialTheme.colorScheme.outlineVariant
            ShuffleMode.Enabled -> MaterialTheme.colorScheme.secondary
        }
        Icon(
            imageVector = Icons.Rounded.Shuffle,
            contentDescription = "Shuffle mode",
            tint = tint
        )
    }
}

enum class ShuffleMode { Enabled, Disabled }

@Composable
private fun RepeatButton(
    repeatMode: RepeatMode,
    onRepeatModeChanged: (newRepeatMode: RepeatMode) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = {
            val newRepeatMode = when (repeatMode) {
                RepeatMode.List -> RepeatMode.One
                RepeatMode.One -> RepeatMode.Disabled
                RepeatMode.Disabled -> RepeatMode.List
            }
            onRepeatModeChanged.invoke(newRepeatMode)
        },
        modifier = modifier.size(48.dp)
    ) {
        val imageVector = when (repeatMode) {
            RepeatMode.List,
            RepeatMode.Disabled -> Icons.Rounded.Repeat

            RepeatMode.One -> Icons.Rounded.RepeatOne
        }
        val tint = when (repeatMode) {
            RepeatMode.List,
            RepeatMode.One -> MaterialTheme.colorScheme.secondary

            RepeatMode.Disabled -> MaterialTheme.colorScheme.outlineVariant
        }
        Icon(
            imageVector = imageVector,
            contentDescription = "Repeat mode",
            tint = tint
        )
    }
}

enum class RepeatMode { Disabled, One, List }

@Composable
private fun ArrowButton(
    direction: ArrowDirection,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(64.dp)
    ) {
        when (direction) {
            ArrowDirection.Next -> {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Next song",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = modifier.size(38.dp)
                )
            }

            ArrowDirection.Previous -> {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = "Previous song",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = modifier.size(38.dp)
                )
            }
        }
    }
}

enum class ArrowDirection { Previous, Next }

@Composable
private fun PlayPauseButton(
    isPlayed: State<Boolean>,
    onPlayedChanged: (isPlayed: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconToggleButton(
        checked = isPlayed.value,
        onCheckedChange = { onPlayedChanged.invoke(it) },
        modifier = modifier.size(64.dp)
    ) {
        if (isPlayed.value) {
            Icon(
                imageVector = Icons.Rounded.Pause,
                modifier = Modifier.size(48.dp),
                contentDescription = "Pause button"
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                modifier = Modifier.size(48.dp),
                contentDescription = "Play button"
            )
        }
    }
}