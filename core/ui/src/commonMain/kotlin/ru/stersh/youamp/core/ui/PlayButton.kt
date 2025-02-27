package ru.stersh.youamp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlayButtonOutlined(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = CircleShape
            )
            .size(48.dp)
    ) {
        PlayButton(
            isPlaying = isPlaying,
            onClick = onClick
        )
    }
}

@Composable
fun PlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(),
        modifier = modifier
    ) {
        val icon = if (isPlaying) {
            Icons.Rounded.Pause
        } else {
            Icons.Rounded.PlayArrow
        }
        Icon(
            imageVector = icon,
            tint = MaterialTheme.colorScheme.onSecondary,
            contentDescription = null
        )
    }
}

@Composable
@Preview
private fun OutlinedPlayButtonPreview() {
    MaterialTheme {
        PlayButtonOutlined(
            isPlaying = true,
            onClick = {}
        )
    }
}