package ru.stresh.youamp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.SubcomposeAsyncImage

@Composable
@Preview(showBackground = true)
private fun ArtworkPreview() {
    Artwork(artworkUrl = "https://placehold.co/400")
}

@Composable
fun Artwork(
    artworkUrl: String?,
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = MaterialTheme.shapes.large
) {
    SubcomposeAsyncImage(
        model = artworkUrl,
        contentDescription = "Album image",
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape),
        loading = {
            ArtworkPlaceholder()
        },
        error = {
            ArtworkPlaceholder()
        }
    )
}

@Composable
fun ArtworkPlaceholder() {
    Icon(
        imageVector = Icons.Rounded.Album,
        contentDescription = "placeholder",
        tint = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
    )
}