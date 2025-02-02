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
fun AlbumItem(
    title: String,
    onClick: () -> Unit,
    artist: String? = null,
    artworkUrl: String? = null,
    playButton: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        onClick = onClick,
        modifier = modifier
    ) {
        AlbumLayout(
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
            artist = {
                if (artist != null) {
                    Text(
                        text = artist,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Left,
                        minLines = 1,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
private fun AlbumLayout(
    title: @Composable () -> Unit,
    artist: @Composable () -> Unit,
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
            artist,
            artwork,
            playButton
        ),
        modifier = modifier
    ) { measurables, constraints ->

        val titlePlaceable = measurables[0]
            .first()
            .measure(constraints)
        val artistPlaceable = measurables
            .getOrNull(1)
            ?.firstOrNull()
            ?.measure(constraints)
        val artworkPlaceable = measurables[2]
            .first()
            .measure(Constraints.fixed(constraints.maxWidth, constraints.maxWidth))
        val playButtonPlaceable = measurables
            .getOrNull(3)
            ?.firstOrNull()
            ?.measure(constraints)

        val maxHeight = titlePlaceable.height + (artistPlaceable?.height ?: 0) +
                artworkPlaceable.height + ((playButtonPlaceable?.height ?: 0) / 2) + titleTopPadding + contentPadding

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
            artistPlaceable?.placeRelative(contentPadding, titleY + artistPlaceable.height)
        }
    }
}

@Composable
@Preview
private fun AlbumItemPreview() {
    MaterialTheme {
        AlbumItem(
            title = "Album",
            artist = "artist",
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