package ru.stersh.youamp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun ArtistItem(
    name: String,
    onClick: () -> Unit,
    artworkUrl: String? = null,
    modifier: Modifier = Modifier
) {
    ArtistLayout(
        name = {
            ArtistTitle(name)
        },
        artwork = {
            ArtistArtwork(artworkUrl)
        },
        modifier = modifier
            .clip(ArtistShape)
            .clickable(onClick = onClick)
    )
}

@Composable
fun ArtistItem(
    name: String,
    playButton: @Composable () -> Unit,
    onClick: () -> Unit,
    artworkUrl: String? = null,
    modifier: Modifier = Modifier
) {
    ArtistLayout(
        name = {
            ArtistTitle(name)
        },
        artwork = {
            ArtistArtwork(artworkUrl)
        },
        playButton = playButton,
        modifier = modifier
            .clip(ArtistShape)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun ArtistTitle(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = name,
        textAlign = TextAlign.Center,
        minLines = 2,
        maxLines = 2,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun ArtistArtwork(
    artworkUrl: String?,
    modifier: Modifier = Modifier
) {
    CircleArtwork(
        artworkUrl = artworkUrl,
        placeholder = Icons.Rounded.Person,
        modifier = modifier
    )
}

@Composable

private fun PlayButtonBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.2f to Color.Transparent,
                        0.7f to MaterialTheme.colorScheme.surfaceContainerLow
                    )
                ),
                shape = CircleShape
            )
    )
}

@Composable
private fun ArtistLayout(
    name: @Composable () -> Unit,
    artwork: @Composable () -> Unit,
    playButton: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val titleTopPadding = 8.dp.roundToPx(density)
    val contentPadding = 12.dp.roundToPx(density)
    val playButtonPadding = 8.dp.roundToPx(density)
    Layout(
        contents = listOf(
            name,
            artwork,
            playButton,
            {
                PlayButtonBackground()
            }
        ),
        modifier = modifier
    ) { measurables, constraints ->

        val artworkConstraints = Constraints.fixed(constraints.maxWidth, constraints.maxWidth)
        val titlePlaceable = measurables[0]
            .first()
            .measure(constraints)
        val artworkPlaceable = measurables[1]
            .first()
            .measure(artworkConstraints)
        val playButtonPlaceable = measurables[2]
            .first()
            .measure(constraints.copy(minWidth = 0, minHeight = 0))
        val playButtonBackgroundPlaceable = measurables[3]
            .first()
            .measure(artworkConstraints)

        val maxHeight = titlePlaceable.height + artworkPlaceable.height + titleTopPadding + contentPadding

        layout(artworkPlaceable.width, maxHeight) {
            artworkPlaceable.placeRelative(0, 0)
            playButtonBackgroundPlaceable.placeRelative(0, 0)
            playButtonPlaceable.placeRelative(
                (artworkPlaceable.width / 2) - (playButtonPlaceable.width / 2),
                artworkPlaceable.height - playButtonPlaceable.height - playButtonPadding
            )
            titlePlaceable.placeRelative(0, artworkPlaceable.height + titleTopPadding)
        }
    }
}

@Composable
private fun ArtistLayout(
    name: @Composable () -> Unit,
    artwork: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val titleTopPadding = 8.dp.roundToPx(density)
    val contentPadding = 12.dp.roundToPx(density)
    Layout(
        contents = listOf(
            name,
            artwork
        ),
        modifier = modifier
    ) { measurables, constraints ->

        val titlePlaceable = measurables[0]
            .first()
            .measure(constraints)
        val artworkPlaceable = measurables[1]
            .first()
            .measure(Constraints.fixed(constraints.maxWidth, constraints.maxWidth))

        val maxHeight = titlePlaceable.height + artworkPlaceable.height + titleTopPadding + contentPadding

        layout(artworkPlaceable.width, maxHeight) {
            artworkPlaceable.placeRelative(0, 0)
            titlePlaceable.placeRelative(0, artworkPlaceable.height + titleTopPadding)
        }
    }
}

@Stable
private val ArtistShape = RoundedCornerShape(36.dp)

@Composable
@Preview
private fun ArtistItemPreview() {
    MaterialTheme {
        Column {
            ArtistItem(
                name = "Name",
                onClick = {}
            )
            ArtistItem(
                name = "Name",
                onClick = {},
                playButton = {
                    PlayButtonOutlined(
                        isPlaying = false,
                        onClick = {}
                    )
                },
            )
        }
    }
}