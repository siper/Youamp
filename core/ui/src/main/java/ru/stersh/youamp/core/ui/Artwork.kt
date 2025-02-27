package ru.stersh.youamp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun Artwork(
    artworkUrl: String?,
    placeholder: ImageVector,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    shape: CornerBasedShape = MaterialTheme.shapes.large
) {
    SubcomposeAsyncImage(
        model = artworkUrl,
        contentDescription = stringResource(R.string.placeholder_image_description),
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        contentScale = contentScale,
        loading = {
            ArtworkPlaceholder(
                placeholder = placeholder
            )
        },
        error = {
            ArtworkPlaceholder(
                placeholder = placeholder
            )
        }
    )
}

@Composable
fun CircleArtwork(
    artworkUrl: String?,
    placeholder: ImageVector,
    modifier: Modifier = Modifier
) {
    Artwork(
        artworkUrl = artworkUrl,
        placeholder = placeholder,
        contentScale = ContentScale.Crop,
        shape = CircleShape,
        modifier = modifier.aspectRatio(1f)
    )
}

@Composable
fun ArtworkPlaceholder(
    placeholder: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = placeholder,
            contentDescription = "placeholder",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxSize(0.75f)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ArtworkPreview() {
    YouampPlayerTheme {
        Surface {
            Column {
                Artwork(
                    artworkUrl = "https://placehold.co/400",
                    placeholder = Icons.Rounded.MusicNote,
                    modifier = Modifier.size(40.dp)
                )
                Artwork(
                    artworkUrl = "https://placehold.co/400",
                    placeholder = Icons.Rounded.Person,
                    modifier = Modifier.size(60.dp)
                )
                Artwork(
                    artworkUrl = "https://placehold.co/400",
                    placeholder = Icons.Rounded.Album,
                    modifier = Modifier.size(80.dp)
                )
                CircleArtwork(
                    artworkUrl = "https://placehold.co/400",
                    placeholder = Icons.Rounded.Person,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}