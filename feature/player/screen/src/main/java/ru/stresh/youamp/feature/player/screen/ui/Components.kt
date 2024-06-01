package ru.stresh.youamp.feature.player.screen.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.stersh.youamp.feature.player.big.R

@Composable
internal fun PlayQueueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.QueueMusic,
            contentDescription = stringResource(R.string.play_queue_description)
        )
    }
}

@Composable
internal fun ShuffleButton(
    shuffleMode: ShuffleModeUi,
    onShuffleModeChanged: (newShuffleMode: ShuffleModeUi) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = shuffleMode == ShuffleModeUi.Enabled,
        onCheckedChange = {
            val newShuffleMode = when (shuffleMode) {
                ShuffleModeUi.Enabled -> ShuffleModeUi.Disabled
                ShuffleModeUi.Disabled -> ShuffleModeUi.Enabled
            }
            onShuffleModeChanged.invoke(newShuffleMode)
        },
        modifier = modifier.size(64.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Shuffle,
            contentDescription = stringResource(R.string.shuffle_mode_description)
        )
    }
}

@Composable
internal fun RepeatButton(
    repeatMode: RepeatModeUi,
    onRepeatModeChanged: (newRepeatMode: RepeatModeUi) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = repeatMode == RepeatModeUi.One || repeatMode == RepeatModeUi.All,
        onCheckedChange = {
            val newRepeatMode = when (repeatMode) {
                RepeatModeUi.All -> RepeatModeUi.One
                RepeatModeUi.One -> RepeatModeUi.Disabled
                RepeatModeUi.Disabled -> RepeatModeUi.All
            }
            onRepeatModeChanged.invoke(newRepeatMode)
        },
        modifier = modifier.size(64.dp)
    ) {
        val imageVector = when (repeatMode) {
            RepeatModeUi.All,
            RepeatModeUi.Disabled -> Icons.Rounded.Repeat

            RepeatModeUi.One -> Icons.Rounded.RepeatOne
        }
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(R.string.repeat_mode_description)
        )
    }
}

@Composable
internal fun FavoriteButton(
    isFavorite: Boolean,
    onFavoriteChanged: (isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = onFavoriteChanged,
        modifier = modifier.size(64.dp)
    ) {
        val imageVector = if (isFavorite) {
            Icons.Rounded.Favorite
        } else {
            Icons.Rounded.FavoriteBorder
        }
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(R.string.favorite_song_description)
        )
    }
}

@Composable
internal fun PlayPauseButton(
    isPlayed: Boolean,
    onPlayedChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledTonalIconButton(
        shape = remember { RoundedCornerShape(24.dp) },
        onClick = { onPlayedChanged() },
        modifier = modifier.size(176.dp, 92.dp)
    ) {
        if (isPlayed) {
            Icon(
                imageVector = Icons.Rounded.Pause,
                contentDescription = stringResource(R.string.pause_button_description)
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = stringResource(R.string.play_button_description)
            )
        }
    }
}

@Composable
internal fun PlayerSlider(
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