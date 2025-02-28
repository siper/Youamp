package ru.stersh.youamp.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SongCardItem(
    title: String,
    artist: String? = null,
    artworkUrl: String? = null,
    playButton: @Composable () -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier) {
        SongItem(
            title = title,
            artist = artist,
            artworkUrl = artworkUrl,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(),
            colors = songItemCardColors(),
            trailingContent = playButton
        )
    }
}

@Composable
fun SongItem(
    title: String,
    artist: String? = null,
    artworkUrl: String? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit = {},
    colors: ListItemColors = ListItemDefaults.colors()
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            artist?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        leadingContent = {
            Artwork(
                artworkUrl = artworkUrl,
                placeholder = Icons.Rounded.MusicNote,
                modifier = Modifier.size(48.dp)
            )
        },
        trailingContent = trailingContent,
        modifier = modifier.clickable(onClick = onClick),
        colors = colors
    )
}

@Stable
private var songItemCardColorsDark: ListItemColors? = null

@Stable
private var songItemCardColorsLight: ListItemColors? = null

@Composable
private fun songItemCardColors(): ListItemColors {
    return if (isSystemInDarkTheme()) {
        songItemCardColorsDark ?: ListItemDefaults
            .colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
            .also { songItemCardColorsDark = it }
    } else {
        songItemCardColorsLight ?: ListItemDefaults
            .colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
            .also { songItemCardColorsLight = it }
    }
}