package ru.stersh.youamp.feature.personal.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
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
fun OutlinedPlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = CircleShape
        )
        .padding(1.dp)
    ) {
        IconButton(
            onClick = onClick,
            colors = IconButtonDefaults.filledIconButtonColors(),
            modifier = Modifier.requiredSize(49.dp)
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
}

@Composable
@Preview
private fun OutlinedPlayButtonPreview() {
    MaterialTheme {
        OutlinedPlayButton(
            isPlaying = true,
            onClick = {}
        )
    }
}