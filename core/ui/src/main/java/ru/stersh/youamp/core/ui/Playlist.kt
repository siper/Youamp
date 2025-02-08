package ru.stersh.youamp.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp


@Composable
fun PlaylistItem(
    title: String,
    onClick: () -> Unit,
    artworkUrl: String? = null,
    playButton: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        onClick = onClick,
        modifier = modifier
    ) {
        PlaylistLayout(
            title = {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    minLines = 1,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge
                )
            },
            artwork = {
                Artwork(
                    artworkUrl = artworkUrl,
                    placeholder = Icons.Rounded.Album
                )
            },
            playButton = playButton
        )
    }
}

@Composable
private fun PlaylistLayout(
    title: @Composable () -> Unit,
    artwork: @Composable () -> Unit,
    playButton: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val contentPadding = 12.dp.roundToPx(density)
    val titleTopPadding = 4.dp.roundToPx(density)
    Layout(
        contents = listOf(
            title,
            artwork,
            playButton
        ),
        modifier = modifier
    ) { measurables, constraints ->

        val titlePlaceable = measurables[0]
            .first()
            .measure(constraints)
        val artworkPlaceable = measurables[1]
            .first()
            .measure(Constraints.fixed(constraints.maxWidth, constraints.maxWidth))
        val playButtonPlaceable = measurables
            .getOrNull(2)
            ?.firstOrNull()
            ?.measure(constraints)

        val maxHeight =
            titlePlaceable.height + artworkPlaceable.height + titleTopPadding + contentPadding + ((playButtonPlaceable?.height
                ?: 0) / 2)

        layout(artworkPlaceable.width, maxHeight) {
            artworkPlaceable.placeRelative(0, 0)
            playButtonPlaceable?.placeRelative(
                (artworkPlaceable.width / 2) - (playButtonPlaceable.width / 2),
                artworkPlaceable.height - (playButtonPlaceable.height / 2)
            )
            val titleY = if (playButtonPlaceable != null) {
                artworkPlaceable.height + titleTopPadding + (playButtonPlaceable.height / 2)
            } else {
                artworkPlaceable.height + titleTopPadding
            }
            titlePlaceable.placeRelative(contentPadding, titleY)
        }
    }
}

@Composable
@Preview
private fun PlaylistItemPreview() {
    MaterialTheme {
        PlaylistItem(
            title = "Playlist",
            playButton = {
                PlayButtonOutlined(
                    isPlaying = false,
                    onClick = {},
                )
            },
            onClick = {}
        )
    }
}